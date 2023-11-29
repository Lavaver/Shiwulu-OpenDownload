# Shiwulu OpenDownload · 一款由 Java 开发的自由下载本体

若你需要英文版，请[参阅此处](/README-EN.md)

极为高效的设计，配合多线程与轻量化的技术，这是一个奇迹，也是应该做的。

## 功能特点

- 多线程下载：利用多线程技术提高下载速度。
- 显示下载进度：实时显示已下载数据大小、总大小、进度百分比等信息。
- 估计剩余时间：根据已下载数据大小和下载速度估计剩余下载时间。
- 系统托盘通知：在下载开始和下载完成时通过系统托盘通知提醒用户。

## 如何使用

1. 克隆或下载此仓库到本地计算机。
> 以 Visual Studio Code 为例，打开克隆好的文件夹，定位到工程文件，然后按下 ``Ctrl+Shift+P`` 呼出任务面板，搜索 jar 关键词，点击打包成 jar ，按默认设置等待编译完成即可。

> 如果你有 Java 开发套件 8（JDK 8），那么你可以使用 javac 命令编译。

> 该项目仓库的 Release 页有已经编译好的 jar 文件可供使用。
2. 运行 `Shiwulu.OpenDownload.jar` 文件以启动下载程序的GUI界面。
> 如果双击运行未出现 GUI ，请打开 bash ，然后运行 ``java -jar Shiwulu.OpenDownload.jar``
3. 在界面中输入下载链接和保存路径，并点击开始按钮开始下载。

## 注意事项

- 请确保系统具有足够的磁盘空间来保存下载的文件。
- 当下载过程中出现错误时，程序会显示错误信息并给出相应的解决方案建议。
- 在部分情况下，百度网盘或GitHub链接可能会导致程序出错，请尽量避免使用这些链接进行下载。
- 请**时刻保持跟进最新版本**，因为最新版本可能包含重要的安全与质量补丁。
- 本作者已经**强烈不推荐使用受 Log4j2 影响的版本**，这会导致严重事故。
- 本体最低运行 Java 版本为 1.8（Java 8），低于此版本可能导致一系列兼容问题。

## 贡献指南

如果你希望为项目做出贡献，你可以按照以下步骤进行：

1. 在GitHub上Fork此仓库。
2. 创建一个新的分支：`git checkout -b feature/your-feature-name`。
3. 进行必要的更改并提交你的分支：`git commit -am 'Add some feature'`。
4. 推送到分支：`git push origin feature/your-feature-name`。
5. 提交Pull请求。

## 问题反馈

如果你在使用项目过程中遇到任何问题，或者有任何建议和改进意见，请在 [GitHub Issues](https://github.com/Lavaver/Shiwulu-OpenDownload/issues) 中提出。

感谢你对项目的贡献！

## ENJOY!
