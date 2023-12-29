import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Toolkit;
import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.swing.JOptionPane;

public class Opendownload_main extends Thread {
    private String downloadUrl;
    private String savePath;
    private TrayIcon trayIcon;
    long startTime;
    private long totalDownloaded;
    //我 有 抑 郁 症
    int updateCount;

    public Opendownload_main(String downloadUrl, String savePath) {
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
    }

    private boolean checkDiskSpace() {
        File saveFolder = new File(savePath);
        long requiredSpace = 0;

        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            requiredSpace = connection.getContentLength();

            
            Path saveFolderPath = saveFolder.toPath();
            FileStore store = Files.getFileStore(saveFolderPath);

            long usableSpace = store.getUsableSpace();
            if (requiredSpace > usableSpace) {
                showDiskSpaceNotification(store, requiredSpace);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void showDiskSpaceNotification(FileStore store, long requiredSpace) {
        String drive = store.toString();
        String title = drive + " 上的空间不足以下载此文件";
        String content = "请考虑更换分区或硬盘并重试";

        showBalloonNotification(title, content);
    }

    @Override
    public void run() {
        try {
            // 检查磁盘空间
            if (!checkDiskSpace()) {
                // 返回到输入存储路径窗口
                main(new String[]{});
                return;
            }

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();
            String fileName = new File(url.getPath()).getName();

            // 显示开始下载的通知
            showNotification(fileName, fileLength, 0, "Calculating...");

            // 检查保存路径是否存在，如果不存在则创建文件夹
            File saveFolder = new File(savePath);
            if (!saveFolder.exists()) {
                if (saveFolder.mkdirs()) {
                    System.out.println("文件夹已创建：" + savePath);
                } else {
                    System.out.println("无法创建文件夹：" + savePath);
                    return; // 如果无法创建文件夹，则退出方法
                }
            }

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(savePath + File.separator + fileName);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            startTime = System.currentTimeMillis();

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
                int progress = (int) (total * 100 / fileLength);

                // 更新下载进度
                updateProgress(fileName, fileLength, total, progress, calculateETA(startTime, total, fileLength));
            }

            // 下载完成的通知
            showNotification(fileName, fileLength, 100, "Download complete");
            System.out.println("下载完成：" + savePath + File.separator + fileName);

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            String errorMessage = "下载时出现错误：" + e.getMessage();
            String stackTrace = getStackTrace(e);
            System.err.println(errorMessage);
            System.err.println("堆栈日志输出的信息：");
            System.err.println(stackTrace);
            System.err.println("请不要将此控制台截图发给别人，这没有任何作用。");
            System.err.println("你应该全选提交控制台输出的所有信息，并前往 https://github.com/Lavaver/Shiwulu-OpenDownload/issues 发 issues 以帮助作者快速定位问题并修复");
            System.err.println("而且，对于部分诸如 CDN 镜像站、百度网盘、GitHub Releases 下载可能会直接报错 403 导致程序出错，请见谅！");

            showErrorMessage(errorMessage);
        }
    }

    private void showNotification(String fileName, long fileSize, int progress, String message) {
        String title = fileName + " 的下载已" + (progress == 0 ? "开始" : "完成");
        String content = (progress == 0 ? "文件大小：" + formatSize(fileSize) + "\n返回到控制台页面以查看详情。" : "文件已下载到 " + savePath + "\n你可能需要按下 Ctrl+C 来关闭此程序");
        showBalloonNotification(title, content);
    }

    private void updateProgress(String fileName, long fileSize, long downloaded, int progress, String eta) {
        String progressInfo = "已接收：" + formatSize(downloaded) + " / " + formatSize(fileSize) + " 数据" + " | 进度：" + progress + "% | 剩余时间（ETA）：" + eta;

        // 计算下载速度
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.startTime;
        double speed = (double) downloaded / elapsedTime;
        String speedInfo = "下载速度：" + formatSize((long) speed) + "/s";

        // 计算平均下载速度
        totalDownloaded += downloaded;
        updateCount++;
        double averageSpeed = (double) totalDownloaded / (elapsedTime / 1000);
        String averageSpeedInfo = "平均速度：" + formatSize((long) averageSpeed) + "/s";

        // 将下载速度和平均下载速度添加到进度信息中
        progressInfo += " | " + speedInfo + " | " + averageSpeedInfo;

        System.out.println(progressInfo);
    }

    private String calculateETA(long startTime, long downloaded, long fileSize) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long remainingTime = (fileSize - downloaded) * elapsedTime / downloaded;
        return remainingTime / 1000 + " 秒";
    }

    private String formatSize(long size) {
        if (size < 1024) {
            return size + " 字节";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KiB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MiB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GiB", size / (1024.0 * 1024 * 1024));
        }
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private void showBalloonNotification(String title, String message) {
        if (!SystemTray.isSupported()) {
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        try {
            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("icon.png"), "下载通知");
            trayIcon.setImageAutoSize(true);

            PopupMenu popupMenu = new PopupMenu();
            MenuItem exitMenuItem = new MenuItem("退出");
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
            popupMenu.add(exitMenuItem);
            trayIcon.setPopupMenu(popupMenu);

            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "下载出错", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            String downloadUrl = JOptionPane.showInputDialog("请输入下载地址：\n请避免使用百度网盘或 GitHub 链接，因为会出错（");
            if (downloadUrl == null) {

    

return;
}

        String savePath = JOptionPane.showInputDialog("请输入保存路径：");
        if (savePath == null) {
            return;
        }

        Opendownload_main downloader = new Opendownload_main(downloadUrl, savePath);
        downloader.start();
    } else if (args.length == 2) {
        String downloadUrl = args[0];
        String savePath = args[1];
        
        Opendownload_main downloader = new Opendownload_main(downloadUrl, savePath);
        downloader.start();
        return; // 添加此行以确保程序在执行完毕后退出
    } else if (args.length == 3 && args[0].equals("-quickdownload")) {
        String downloadUrl = args[1];
        String savePath = args[2];
        
        Opendownload_main downloader = new Opendownload_main(downloadUrl, savePath);
        downloader.start();
        return; // 添加此行以确保程序在执行完毕后退出
        
    } else if (args.length == 1 && args[0].equals("-about")) {
        System.out.println("Shiwulu OpenDownload");
        System.out.println("请支持自由软件事业的开发，谢谢！");
        System.out.println("如果你是通过购买而来的此发行版本体，那么你应该要求退款，并做法律程序。");
        System.out.println("由 Lavaver 开发、发行的实用下载本体。1.1.2.70c LTS 发行版");

    } else if (args.length == 1 && args[0].equals("-help")) {
        System.out.println("帮助");
        System.out.println("----------------");
        System.out.println("下载文件请直接启动本体，或使用 -quickdownload [下载地址] [保存路径] 快速开始一个新下载。");
        System.out.println("使用 -nogui 以在无图形化界面下启动程序");
        System.out.println("使用 -about 获取发行版本体相关信息，使用 -help 呼出此页。");
        System.out.println("使用 -updatelog 呼出更新日志");

    } else if (args.length == 1 && args[0].equals("-updatelog")) {
        System.out.println("更新日志（LTS 70c2 版本）");
        System.out.println("----------------");
        System.out.println("- 添加 -nogui 参数，使其可以在无图形化界面情况下使用。");
        System.out.println("有关详细信息，请参阅 https://github.com/Lavaver/Shiwulu-OpenDownload/releases");

    } else if (args.length == 1 && args[0].equals("-nogui")) {
        runWithoutGUI();

    } else {
        System.out.println("参数错误：请提供该参数正确的值（如：-quickdownload 后需要跟链接和保存路径）");
    }

    
}

private static void runWithoutGUI() {
    String downloadUrl = getInput("请输入下载地址：\n请避免使用百度网盘或 GitHub 链接，因为会出错（");
    if (downloadUrl == null) {
        return;
    }

    String savePath = getInput("请输入保存路径：");
    if (savePath == null) {
        return;
    }

    Opendownload_main downloader = new Opendownload_main(downloadUrl, savePath);
    downloader.start();
}

private static String getInput(String message) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println(message);
    try {
        return reader.readLine();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

}