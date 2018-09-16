package airconditioner20180908;

import java.util.Scanner;

public class AirConditioner20180908 {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int currentRoomTemperature = scanner.nextInt();
		int presetRoomTemperature = scanner.nextInt();

		AirConditioner20180908 airCon = new AirConditioner20180908();
		if (!airCon.isFrom0To40Num(currentRoomTemperature, presetRoomTemperature)) {
			throw new Exception("入力された値が不正です。");
		}

		int max = Math.max(currentRoomTemperature, presetRoomTemperature);
		int min = Math.min(currentRoomTemperature, presetRoomTemperature);
		int diff = max - min;

		int count = 0;
		while (!(diff == 0)) {
			if (diff >= 10) {
				diff -= 10;
			} else if (diff >= 5) {
				diff -= 5;
			} else if (diff >= 1) {
				diff -= 1;
			}
			count++;
		}

		// 結果を出力
		System.out.println(count);
	}

	// 数字の内容を確認
	private boolean isFrom0To40Num(int num1, int num2) {
		if (!(0 <= num1) || !(num1 <= 40) || !(0 <= num2) || !(num2 <= 40)) {
			return false;
		}
		return true;
	}
}
