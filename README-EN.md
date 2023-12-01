# Shiwulu OpenDownload · A Java-based Free Download Tool

If you need the Chinese version, please refer to [here](/README.md)

高效的设计结合多线程和轻量化技术，这是一个奇迹，也是应该做的。

## Features

- Multi-threaded Download: Utilizes multi-threading technology to improve download speed.
- Display Download Progress: Real-time display of downloaded data size, total size, progress percentage, and more.
- Estimated Remaining Time: Estimates remaining download time based on downloaded data size and download speed.
- System Tray Notifications: Notifies users through system tray notifications when the download starts and completes.

## How to Use

1. Clone or download this repository to your local computer.
> Take Visual Studio Code as an example, open the cloned folder, locate the project file, then press ``Ctrl+Shift+P`` to call up the task panel, search for jar keywords, click to ``Java: Export Jar``, and wait for the compilation to complete according to the default settings.

> If you have a Java Development Kit 8 (JDK 8), then you can compile using the javac command.

> The release page of the project's repository has compiled jar files ready for use. 

2. Run the `Shiwulu.OpenDownload.jar` file to launch the GUI interface for the download program.
> If double-clicking does not open the GUI, please open a bash terminal and run ``java -jar Shiwulu.OpenDownload.jar``

> If you get the jar file by compilation, the file name may be slightly different, depending on the actual situation.

> The standard command to run a jar file is ``java -jar [filename].jar``

3. Enter the download link and the save path in the interface, and click the start button to begin the download.
> The download process requires an internet connection.

> The multi-threaded download speed of the downloader depends on the bandwidth limit. If the bandwidth limit is low, it will have a slight impact on the download speed.

> If using a mobile hotspot, it is recommended to use a USB wired hotspot connection. When the network status of the carrier fluctuates significantly, it will severely affect the download speed.

## Notes

- Ensure that the system has sufficient disk space to save the downloaded files.
- When errors occur during the download process, the program will display error messages and provide corresponding solution suggestions.
- In some cases, using links from Baidu Cloud or GitHub may cause errors in the program. Please try to avoid using these links for download.
- **Always keep up with the latest version**, as the latest version may contain important security and quality patches.
- The author **strongly recommends against using versions affected by Log4j2**, as this could lead to serious incidents.
- The minimum required Java version for running the tool is 1.8 (Java 8). Running on versions lower than this may lead to compatibility issues.

## Solutions for Compilation Errors Caused by Code Issues

- Possible missing references in the source code project causing the error. Restore the references as shown below to resolve the issue:
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
- Possible modifications to the main class `public class MultiThreadedDownloader extends Thread {}` due to file name changes:
  - Rename the file to `MultiThreadedDownloader` to restore the relationship with the main class;
  - Change `MultiThreadedDownloader` in `public class MultiThreadedDownloader extends Thread {}` to the current project file name to restore the relationship with the main class.
- Syntax errors caused by typos in critical sections of code:
  - Correct the syntax;
  - If you are using Visual Studio, switch the IDE to the Code version and correct the functional error as prompted.
- Unable to compile and run in Debug mode:
  - If you are using Visual Studio, switch the IDE to the Code version and it will automatically find JDK and compile and run.
- Strikethrough statements:
  - The method you are using has been deprecated in your JDK version. If you want to be compatible with higher versions of Java, you may need to modify the relevant statements.
- "`[METHOD] cannot be resolved to a type`" error:
  - You accidentally deleted the reference required for this method. For example, "`TrayIcon cannot be resolved to a type`" is caused by the lack of "`import java.awt.TrayIcon;`" reference. The specific situation requires adding references according to the actual situation.
- "`Syntax error on token [MAIN CLASS], Identifier expected`" error:
  - You accidentally placed the project file outside the `src` directory of the project, putting it back in the `src` directory will allow the project file to be recognized again.

## Contribution Guide

If you wish to contribute to the project, you can follow these steps:

1. Fork this repository on GitHub.
2. Create a new branch: `git checkout -b feature/your-feature-name`.
3. Make necessary changes and commit them to your branch: `git commit -am 'Add some feature'`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Submit a pull request.

## Issue Feedback

If you encounter any problems while using the project, or have any suggestions and improvement ideas, please submit them in [GitHub Issues](https://github.com/Lavaver/Shiwulu-OpenDownload/issues).

Thank you for your contributions to the project!

## ENJOY!
