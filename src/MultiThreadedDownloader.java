import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.swing.JOptionPane;

public class MultiThreadedDownloader extends Thread {
    private String downloadUrl;
    private String savePath;

    public MultiThreadedDownloader(String downloadUrl, String savePath) {
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        try {
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
            long startTime = System.currentTimeMillis();

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
            e.printStackTrace();
        }
    }

    private void showNotification(String fileName, long fileSize, int progress, String message) {
        String title = fileName + " 的下载已" + (progress == 0 ? "开始" : "完成");
        String content = (progress == 0 ? "文件大小：" + fileSize + "，" : "文件已下载到 " + savePath);
        content += "总进度：" + progress + "%，剩余时间（ETA）：" + message;
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateProgress(String fileName, long fileSize, long downloaded, int progress, String eta) {
        String progressInfo = "已下载：" + downloaded + " / " + fileSize + "字节" + "，进度：" + progress + "%，剩余时间（ETA）：" + eta;
        System.out.println(progressInfo);
    }

    private String calculateETA(long startTime, long downloaded, long fileSize) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long remainingTime = (fileSize - downloaded) * elapsedTime / downloaded;
        return remainingTime / 1000 + " 秒";
    }

    public static void main(String[] args) {
        String downloadUrl = JOptionPane.showInputDialog("请输入下载地址：\n请避免使用百度网盘或 GitHub 链接，因为会出错（");
        if (downloadUrl == null) {
            System.out.println("用户取消了下载操作，程序退出。");
            return; // 用户取消输入，则退出程序
        }
    
        String savePath = JOptionPane.showInputDialog("请输入保存路径：");
        if (savePath == null) {
            System.out.println("用户取消了输入保存路径，程序退出。");
            return; // 用户取消输入，则退出程序
        }
    
        MultiThreadedDownloader downloader = new MultiThreadedDownloader(downloadUrl, savePath);
        downloader.start();
    }
}