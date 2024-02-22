import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Scanner;

public class smallgame {

    private static final String DEVELOPER_DEBUG_CODE = "Developer";

    // ANSI颜色代码
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static Object main;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("欢迎来到猜数字游戏！请选择模式：");
        System.out.println("1. 常规模式（猜整数）");
        System.out.println("2. 浮点模式（猜浮点数）");

        int mode = scanner.nextInt();
        if (mode == 1) {
            playNormalMode(scanner, random);
        } else if (mode == 2) {
            playFloatMode(scanner, random);
        } else {
            System.out.println("无效的模式选择！");
        }

        scanner.close();
    }

    private static void playNormalMode(Scanner scanner, Random random) {
        int target = random.nextInt(9901) + 100; // 生成100~10000之间的随机整数
        int guess = 0;

        System.out.println("随机数已生成，请开始猜数（100~10000之间）：");

        while (guess != target) {
            guess = scanner.nextInt();
            if (guess < target) {
                System.out.println("比" + guess + "大，猜错了！");
            } else if (guess > target) {
                System.out.println("比" + guess + "小，猜错了！");
            }
        }

        System.out.println("恭喜你，猜对了！");
    }


    private static void playFloatMode(Scanner scanner, Random random) {
        double target = random.nextDouble() * 3; // 生成0~3之间的随机浮点数
        double guess = 0;

        double bestErrorPercentage = 100.0; // 初始化最佳误差百分比为100%
        double bestGuess = target; // 初始化最佳猜测值为目标值

        System.out.println("随机浮点数已生成，请开始猜数（保留四位小数）：");

        while (true) {
            for (int i = 0; i < 3; i++) {
                String input = scanner.next();
                if (input.equalsIgnoreCase(DEVELOPER_DEBUG_CODE)) {
                    System.out.println(ANSI_YELLOW + "你使用了开发者作弊码调试本局游戏" + ANSI_RESET);
                    System.out.println(ANSI_YELLOW + "由于作弊，你无法在本局游戏中查询分数。" + ANSI_RESET);
                    System.out.println(ANSI_YELLOW + "作弊千万条，诚信第一条；\n滥用作弊码，只有铁窗泪。" + ANSI_RESET);
                    System.out.println(ANSI_YELLOW + "本作者恳求你一句：\n踏实的去做个人吧（（（" + ANSI_RESET);
                    showTrayNotification("恭喜你，挑战成功！", "你使用了作弊码调试本局游戏\n你无法在本局游戏中查询分数。");
                    return;
                }

                guess = Double.parseDouble(input);

                double deviationPercentage = (Math.abs(guess - target) / target) * 100;
                System.out.printf("所输入的浮点值与预期值的误差为 ±%.2f%%\n", deviationPercentage);

                if (deviationPercentage <= 5.0) {
                    System.out.println("恭喜你，挑战成功！");
                    System.out.printf("准确的浮点数为 %.4f\n", target);
                    System.out.printf("本局游戏最佳差值百分比为 ±%.2f%%\n", bestErrorPercentage);
                    showTrayNotification("挑战成功", "准确的浮点数为 " + target + "\n最佳差值为 ±" + bestErrorPercentage);
                    return;
                }

                if (deviationPercentage < bestErrorPercentage) {
                    bestErrorPercentage = deviationPercentage;
                    bestGuess = guess;
                }
            }

            System.out.println("很遗憾，未能猜中。准确的浮点数为：" + target);
            System.out.println("最佳猜测值为：" + bestGuess);
            System.out.printf("本局游戏最佳差值百分比为 ±%.2f%%\n", bestErrorPercentage);
            System.out.println("回答错误，请重新开始猜数（保留四位小数）：");
            showTrayNotification("挑战失败", "准确的浮点数为 " + target);
            target = random.nextDouble() * 3; // 生成新的目标浮点数
            bestErrorPercentage = 100.0; // 重置最佳误差百分比为100%
            bestGuess = target; // 重置最佳猜测值为目标值
        }
    }

    private static void showTrayNotification(String title, String message) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "小游戏");
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("系统不支持系统托盘功能");
        }
    }
}