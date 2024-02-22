
# Shiwulu OpenDownload - a free download program with mystical powers

If you need the Chinese version, please [see here](/README.md).

Extremely efficient design, with multi-threading and lightweight technology, this is a ~~miracle~~ (crossed out) along with the technology and hard to live in Xi'an and Weinan continue to code that unbearable s**t code, is also ~~I have nothing to do myself just do it~~ (crossed out again).

*once written everywhere debugging belongs to be*

## Functional features

- Multi-threaded download: Utilizes multi-threading technology to increase download speed.
- Display download progress: real-time display of downloaded data size, total size, progress percentage and other information.
- Estimated Remaining Time: Estimates the remaining download time based on the downloaded data size and download speed.
- System tray notification: Alerts users via system tray notification when download starts and completes.
- And some other gadgets (Le)

## How to use

1. Clone or download this repository to your local computer.

> Take Visual Studio Code as an example, open the cloned folder, locate the project file, then press ``Ctrl+Shift+P`` to call out the task pane, search for the jar keyword, click ``Java: Export to Jar File``, and then wait for the compilation to complete with the default setting.

> If you have the Java Development Kit 8 (JDK 8), you can use the javac command to compile.

> A compiled jar file is available on the Release page of the project repository.

2. Run the `Shiwulu.OpenDownload.jar` file to launch the downloader's GUI interface.

> If the GUI does not appear when you double-click Run, open bash and run ``java -jar Shiwulu.OpenDownload.jar``.

> If you get the jar file by compiling, the file name may be a little different, as a matter of fact.

> The standard command to run a jar file is ``java -jar [filename].jar``. 3.

3. Enter the download link and save path in the interface and click the Start button to begin the download.

> An Internet connection is required for the download process.

> The multi-threaded download speed of this downloader depends on the bandwidth limit, if the bandwidth limit is low, it will affect the download speed slightly.

> If using a cellular hotspot it is recommended that you use a USB wired hotspot connection. When the carrier's network status fluctuates widely, the download speed will be seriously affected.

## Precautions

- Please make sure your system has enough disk space to save the downloaded files.
- When an error occurs during the download process, the program will display the error message and give you suggestions for a corresponding solution.
- In some cases, Baidu.com or GitHub links may cause errors in the program, please try to avoid using these links for downloading.
- Please **always keep up with the latest version** as the latest version may contain important security and quality patches.
- This author has **strongly discouraged the use of versions affected by Log4j2**, which can lead to serious accidents.
- The minimum running Java version is 1.8 (Java 8), below which a number of compatibility issues may arise.
- If you need to use a parameter such as ``-quickdownload``, please add the parameter **after the **release body name** (example: ``java -jar LTSmain.jar -quickdownload [download link] [save path]``). There is no parameter functionality in versions prior to LTS 1.1.2.70.

## The solution to the problem of not compiling due to code errors

- This may be caused by **missing references** in the source project, restore the references as shown below:

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
```

- The main class has been changed due to a filename change in the ``public class Opendownload_main extends Thread {}`` section involving the main class ``public class Opendownload_main extends Thread {}``:

  - Change filename to ``Opendownload_main`` to restore main class relationship;
  - Change ``Opendownload_main`` in ``public class Opendownload_main extends Thread {}`` to the current project filename to restore the main class relationship.

  > Starting with the LTS 70 release, the old ``MultiThreadedDownloader`` namespace has been replaced by ``Opendownload_main`` and deprecated. If you are still using an older distribution, you may want to consider updating your code or distribution bodies.
  >
- A syntax error involving a critical part of the code resulted:

  - Revised syntax;
  - If you are using Visual Studio, switch the IDE to the Code version and follow the error prompts to correct functional errors.
- Debug run cannot be compiled:

  - If you are using Visual Studio, please switch the IDE to Code version, it will automatically find the JDK and compile it.
- Strikethrough statement appears:

  - The version of the JDK you are using has deprecated this method, and you may need to change the statement if you want to be compatible with higher versions of Java.
- A ``[method] cannot be resolved to a type`` error occurs:

  - You accidentally deleted a reference to this method. In the case of ``TrayIcon cannot be resolved to a type``, this is because you are missing the ``import java.awt.TrayIcon;`` reference. You need to refer to the actual situation to add the reference.
- A ``Syntax error on token [main class], Identifier expected`` error occurs:

  - You accidentally placed the project file somewhere other than the src directory of this project. Put it back in the src directory and the project file will be recognized again.

## Contribution Guidelines

If you wish to contribute to the project, you can follow these steps:

1. Fork this repository on GitHub.
2. Create a new branch: `git checkout -b feature/your-feature-name`.
3. Make the necessary changes and commit your branch: `git commit -am 'Add some feature'`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Commit the Pull request.

## Problem feedback

If you encounter any issues while using the project, or have any suggestions and ideas for improvements, please raise them in [GitHub Issues](https://github.com/Lavaver/Shiwulu-OpenDownload/issues).

Thank you for your contribution to the project!

## ENJOY!
