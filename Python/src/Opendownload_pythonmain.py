import os
import urllib.request
import urllib.error
import time
import math
from datetime import timedelta
import threading


class OpenDownloadMain:
    def __init__(self):
        self.download_url = ""
        self.save_path = ""
        self.start_time = None
        self.total_downloaded = 0
        self.update_count = 0
        self.total = 0
        self.file_length = 0

    def get_user_input(self):
        self.download_url = input("请输入要下载的文件URL: ")
        self.save_path = input("请输入要保存的文件路径: ")

    def download_file(self):
        try:
            with urllib.request.urlopen(self.download_url) as url:
                meta = url.info()
                self.file_length = int(meta["Content-Length"])
                file_name = os.path.basename(urllib.parse.urlparse(self.download_url).path)

                with open(self.save_path, "wb") as f:
                    block_size = 1024
                    self.total = 0
                    self.start_time = time.time()
                    while True:
                        buffer = url.read(block_size)
                        if not buffer:
                            break

                        self.total += len(buffer)
                        f.write(buffer)
                        progress = math.ceil(self.total * 100 / self.file_length)

                        # 更新下载进度
                        self.update_progress(file_name, self.file_length, self.total, progress, self.calculate_eta())

                print("下载完成：", self.save_path)

        except (urllib.error.URLError, OSError) as e:
            error_message = "下载时出现错误：" + str(e)
            print(error_message)

    def update_progress(self, file_name, file_size, downloaded, progress, eta):
        progress_info = f"已接收：{self.format_size(downloaded)} / {self.format_size(file_size)} 数据 | 进度：{progress}% | 剩余时间（ETA）：{eta}"

        # 计算下载速度
        current_time = time.time()
        elapsed_time = current_time - self.start_time
        speed = downloaded / elapsed_time if elapsed_time != 0 else 0
        speed_info = f"下载速度：{self.format_size(speed)}/s"

        # 计算平均下载速度
        self.total_downloaded += downloaded
        self.update_count += 1
        average_speed = self.total_downloaded / (elapsed_time / 1000) if elapsed_time != 0 else 0
        average_speed_info = f"平均速度：{self.format_size(average_speed)}/s"

        # 将下载速度和平均下载速度添加到进度信息中
        progress_info += f" | {speed_info} | {average_speed_info}"

        print(progress_info)

    def calculate_eta(self):
        elapsed_time = time.time() - self.start_time
        remaining_time = (self.file_length - self.total) * elapsed_time / self.total if self.total != 0 else 0
        return str(timedelta(seconds=int(remaining_time)))

    @staticmethod
    def format_size(size):
        if size < 1024:
            return f"{size} 字节"
        elif size < 1024 * 1024:
            return f"{size/1024:.2f} KiB"
        elif size < 1024 * 1024 * 1024:
            return f"{size/(1024*1024):.2f} MiB"
        else:
            return f"{size/(1024*1024*1024):.2f} GiB"

    def run(self):
        self.get_user_input()

        # 多线程下载文件
        download_thread = threading.Thread(target=self.download_file)
        download_thread.start()


if __name__ == '__main__':
    downloader = OpenDownloadMain()
    downloader.run()