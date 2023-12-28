# Shiwulu OpenDownload · 一款由 Java 开发的自由下载本体

若你需要英文版，请 [参阅此处](/README-EN.md)

同时，本作者的该项目也在 [Gitee 码云](https://gitee.com/Lavalive/Shiwulu-OpenDownload) 同步镜像更新。

极为高效的设计，配合多线程与轻量化的技术，这是一个奇迹，也是应该做的。

## 功能特点

- 多线程下载：利用多线程技术提高下载速度。
- 显示下载进度：实时显示已下载数据大小、总大小、进度百分比等信息。
- 估计剩余时间：根据已下载数据大小和下载速度估计剩余下载时间。
- 系统托盘通知：在下载开始和下载完成时通过系统托盘通知提醒用户。

## 如何使用

1. 克隆或下载此仓库到本地计算机。
> 以 Visual Studio Code 为例，打开克隆好的文件夹，定位到工程文件，然后按下 ``Ctrl+Shift+P`` 呼出任务面板，搜索 jar 关键词，点击``Java: 导出到 Jar 文件`` ，按默认设置等待编译完成即可。

> 如果你有 Java 开发套件 8（JDK 8），那么你可以使用 javac 命令编译。

> 该项目仓库的 Release 页有已经编译好的 jar 文件可供使用。

2. 运行 `Shiwulu.OpenDownload.jar` 文件以启动下载程序的GUI界面。
> 如果双击运行未出现 GUI ，请打开 bash ，然后运行 ``java -jar Shiwulu.OpenDownload.jar``

> 如果你是通过编译方式获取的 jar 文件，那么文件名可能会有些许差别，以实际情况为准。

> 运行 jar 文件的标准命令为 ``java -jar [文件名].jar``

3. 在界面中输入下载链接和保存路径，并点击开始按钮开始下载。
> 下载过程中需要有一个 Internet 连接。

> 该下载器多线程下载速度取决于带宽上限，如果带宽上限较低，会对下载速度造成轻微影响。

> 如果使用手机热点建议你使用USB有线热点连接。当运营商网络状态波动幅度较大，下载速度就会造成严重影响。

## 注意事项

- 请确保系统具有足够的磁盘空间来保存下载的文件。
- 当下载过程中出现错误时，程序会显示错误信息并给出相应的解决方案建议。
- 在部分情况下，百度网盘或GitHub链接可能会导致程序出错，请尽量避免使用这些链接进行下载。
- 请**时刻保持跟进最新版本**，因为最新版本可能包含重要的安全与质量补丁。
- 本作者已经**强烈不推荐使用受 Log4j2 影响的版本**，这会导致严重事故。
- 本体最低运行 Java 版本为 1.8（Java 8），低于此版本可能导致一系列兼容问题。
- 如果需要使用例如 ``-quickdownload`` 等参数，请在**发行版本体名称后面再加参数**（示例：``java -jar LTSmain.jar -quickdownload [下载链接] [保存路径]``）。LTS 1.1.2.70 以前的版本没有参数功能。

## 遇到代码错误导致的无法编译的解决方案

- 可能是源代码工程内**缺少引用**导致，将引用恢复到如下所示即可恢复：
```java
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
```
- 在涉及主类 ``public class Opendownload_main extends Thread {}`` 部分因文件名更改导致主类被更改：
  - 修改文件名为 ``Opendownload_main`` 恢复主类关系；
  - 更改 ``public class Opendownload_main extends Thread {}`` 中的 ``Opendownload_main`` 至当前工程文件名恢复主类关系。
  > 从 LTS 70 发行版开始，原先的 ``MultiThreadedDownloader`` 命名空间已被 ``Opendownload_main`` 替代并弃用。若你仍在使用旧的发行版本，则需要考虑更新你的代码或发行版本体。
- 在涉及关键部分代码时犯语法错误导致：
  - 订正语法；
  - 如果你正在使用 Visual Studio ，请将 IDE 切换到 Code 版本，并按错误提示订正功能错误。
- 无法编译 Debug 运行：
  - 如果你正在使用 Visual Studio ，请将 IDE 切换到 Code 版本，它会自动找到 JDK 并编译运行。
- 出现划删除线的语句：
  - 你所使用的 JDK 版本已弃用该方法，如果你想兼容高版本 Java ，则可能需要对相关语句进行更改。
- 出现 ``[方法] cannot be resolved to a type`` 错误：
  - 你意外删除了这个方法需要的引用。以 ``TrayIcon cannot be resolved to a type`` 为例，这因为你缺少了 ``import java.awt.TrayIcon;`` 引用导致的。具体情况需要参考现实情况添加引用。
- 出现 ``Syntax error on token [主类], Identifier expected`` 错误：
  - 你意外将工程文件放置在这个工程 src 目录以外的地方了，放回 src 目录就可以重新识别出工程文件了。

## 发行与旧版生命周期

我，和大家一样的开源社区创作者，自然要通过不断的补丁修复来提高软件易用性。

**长期支持（LTS）版可保有至多 16 年支持期限，并行（TTS）版可保有至多 10 年支持期限，标准版可保有至多 5 年支持期限**

| 版本 | 发行版 | LTS（长期支持） | 生命周期直至 |
| --- | --- | --- | --- |
| 最新版本（Java，1.1.2.x） | √ 是 | √ 是 | 2040/01/10 |
| 1.1 | √ 是 | × 否 | 2029/01/01 |
| 1.0 | √ 是 | × 否 | 2029/01/01 |

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
