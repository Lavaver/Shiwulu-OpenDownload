using System;
using System.IO;
using System.Net.Http;
using System.Diagnostics;
using System.Threading.Tasks;

class OpendownloadMain
{
    private string downloadUrl;
    private string savePath;
    private bool showProgress = false;
    private bool stopDownload = false;
    private long totalDownloaded = 0;
    private int updateCount = 0;
    private Stopwatch stopwatch = new Stopwatch();

    public async Task Run(string[] args)
    {
        await ParseArguments(args);
    }

    private async Task ParseArguments(string[] args)
    {
        if (args.Length < 2)
        {
            ShowHelp();
            return;
        }

        downloadUrl = args[0];
        savePath = args[1];

        for (int i = 2; i < args.Length; i++)
        {
            if (string.Equals(args[i], "-showprogress", StringComparison.OrdinalIgnoreCase))
            {
                showProgress = true;
            }
            else if (string.Equals(args[i], "-stop", StringComparison.OrdinalIgnoreCase))
            {
                stopDownload = true;
            }
            else if (string.Equals(args[i], "-about", StringComparison.OrdinalIgnoreCase))
            {
                ShowAbout();
                return;
            }
            else if (string.Equals(args[i], "-updatelog", StringComparison.OrdinalIgnoreCase))
            {
                ShowUpdateLog();
                return;
            }
            else if (string.Equals(args[i], "-help", StringComparison.OrdinalIgnoreCase))
            {
                ShowHelp();
                return;
            }
        }

        await RunDownload();
    }

    private async Task RunDownload()
    {
        Console.WriteLine($"开始下载：{downloadUrl} 到 {savePath}");

        try
        {
            // 检查保存路径的文件夹是否存在，如果不存在则创建文件夹
            string saveDirectory = Path.GetDirectoryName(savePath);
            if (!Directory.Exists(saveDirectory))
            {
                Directory.CreateDirectory(saveDirectory);
                Console.WriteLine($"文件夹已创建：{saveDirectory}");
            }

            long totalBytes = 0;
            bool isResuming = false;

            if (File.Exists(savePath))
            {
                // 如果文件已经存在，则尝试恢复之前的下载进度
                isResuming = true;
                totalBytes = new FileInfo(savePath).Length;
                Console.WriteLine($"文件已存在：{savePath}，大小：{FormatSize(totalBytes)}");
            }

            using (var httpClient = new HttpClient())
            {
                using (var request = new HttpRequestMessage(HttpMethod.Head, downloadUrl))
                {
                    if (isResuming)
                    {
                        // 如果恢复之前的下载进度，则添加“Range”头部以请求剩余部分的数据
                        request.Headers.Add("Range", $"bytes={totalBytes}-");
                        Console.WriteLine($"正在恢复之前的下载进度，从 {totalBytes} 字节开始下载。");
                    }

                    using (var response = await httpClient.SendAsync(request, HttpCompletionOption.ResponseHeadersRead))
                    {
                        response.EnsureSuccessStatusCode(); // 确保HTTP响应状态为成功

                        var contentLengthHeader = response.Content.Headers.ContentLength;
                        if (contentLengthHeader.HasValue)
                        {
                            totalBytes += contentLengthHeader.Value;
                            Console.WriteLine($"总大小：{FormatSize(totalBytes)}");
                        }
                        else
                        {
                            Console.WriteLine("无法获取文件大小，可能是由于服务器未发送Content-Length头部。");
                        }

                        stopwatch.Start(); // 开始计时

                        using (var inputStream = await response.Content.ReadAsStreamAsync())
                        using (var outputStream = new FileStream(savePath, isResuming ? FileMode.Append : FileMode.Create))
                        {
                            byte[] buffer = new byte[4096];
                            int bytesRead;

                            while ((bytesRead = await inputStream.ReadAsync(buffer, 0, buffer.Length)) > 0)
                            {
                                if (stopDownload) // 如果停止下载已被请求，则退出下载
                                {
                                    Console.WriteLine("下载已被用户中止。");
                                    return;
                                }

                                outputStream.Write(buffer, 0, bytesRead);
                                totalDownloaded += bytesRead;
                                updateCount++;

                                if (showProgress && updateCount % 10 == 0) // 每下载10次更新一次进度信息
                                {
                                    UpdateProgress(totalBytes);
                                }
                            }

                            stopwatch.Stop(); // 停止计时
                            Console.WriteLine($"下载完成：{savePath}，耗时：{stopwatch.ElapsedMilliseconds / 1000.0}秒");
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine($"下载时出现错误：{e.Message}");
        }
    }

    private void UpdateProgress(long fileSize)
    {
        // 计算下载速度
        double speed = (double)totalDownloaded / stopwatch.Elapsed.TotalSeconds;
        string speedInfo = $"下载速度：{FormatSize((long)speed)}/s";

        // 计算平均下载速度
        double averageSpeed = (double)totalDownloaded / stopwatch.Elapsed.TotalSeconds;
        string averageSpeedInfo = $"平均速度：{FormatSize((long)averageSpeed)}/s";

        // 估算剩余时间（ETA）
        long remainingBytes = fileSize - totalDownloaded;
        double remainingSeconds = remainingBytes / speed;
        TimeSpan etaTimeSpan = TimeSpan.FromSeconds(remainingSeconds);
        string eta = $"{(int)etaTimeSpan.TotalHours}h {(int)etaTimeSpan.Minutes}m {(int)etaTimeSpan.Seconds}s";

        string progressInfo = $"已接收：{FormatSize(totalDownloaded)} / {FormatSize(fileSize)} 数据 | 剩余时间（ETA）：{eta} | {speedInfo} | {averageSpeedInfo}";

        Console.WriteLine(progressInfo);
    }

    private string FormatSize(long byteCount)
    {
        string[] suf = { "B", "KB", "MB", "GB", "TB", "PB", "EB" };

        if (byteCount == 0)
            return "0" + suf[0];

        long bytes = Math.Abs(byteCount);
        int place = Convert.ToInt32(Math.Floor(Math.Log(bytes, 1024)));
        double num = Math.Round(bytes / Math.Pow(1024, place), 1);

        return (Math.Sign(byteCount) * num).ToString() + suf[place];
    }

    private void ShowAbout()
    {
        Console.WriteLine("Shiwulu OpenDownload");
        Console.WriteLine("请支持自由软件事业的开发，谢谢！");
        Console.WriteLine("如果你是通过购买而来的此预览版本体，那么你应该要求退款，并做法律程序。");
        Console.WriteLine("由 Lavaver 开发、发行的实用下载本体。24H1 Preview 预览版");
    }

    private void ShowUpdateLog()
    {
        Console.WriteLine("更新日志（Preview 24H1 版本）");
        Console.WriteLine("- 经过几个月的通宵达旦本作者将全新的 .NET 版本公之于众啦");
        Console.WriteLine("- 为 .NET 版新添加了断点续传、可选的终止下载和显示信息功能。超线程抢占模式正在开发。");
        Console.WriteLine("有关详细信息，请参阅 https://github.com/Lavaver/Shiwulu-OpenDownload/releases");
    }

    private void ShowHelp()
    {
        Console.WriteLine("使用方法：OpenDownloadnet [下载链接] [保存路径] <-showprogress> <-stop>");
        Console.WriteLine("-showprogress: （可选）显示下载进度信息");
        Console.WriteLine("-stop: （可选）使用后，可以在下载过程中随时使用回车键终止下载。");
        Console.WriteLine("-about: 显示关于信息");
        Console.WriteLine("-updatelog: 显示更新日志");
        Console.WriteLine("-help: 重新显示该帮助");
    }

    class Program
    {
        static async Task Main(string[] args)
        {
            var opendownload = new OpendownloadMain();
            await opendownload.Run(args);
        }
    }
}