package invoiceperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

public class InvoicePeriod {

	/**
	 *  前給の時のinvoiceを作成する時の処理を再現
	 *
	 *  テスト項目
	 *  ・締め日が単数
	 *  	✔︎現在日時が締め日よりも前
	 *  	✔後
	 *  	✔月末
	 *  ・締め日が複数
	 *  	✔現在時刻が全締め日よりも前
	 *  	✔締め日と締め日の間
	 *  	✔全締め日よりも後
	 *  	✔月末
	 */
	public static void main(String[] args) {
		// 締め日
		String closingDay = "5,15,20,31";

		InvoicePeriod invoicePeriod = new InvoicePeriod();
		int[]intClosingDayArray = invoicePeriod.stringToIntArray(closingDay);

		// 過去6ヶ月間の開始日と締め日のペアを生成する
		System.out.println("[開始日 - 締め日]を新しいものから順番に出力");
		System.out.println(invoicePeriod.createSixMonthsOpeningAndClosingDateList(intClosingDayArray));
	}

	/**
	 * 配列をString型からint型に直して返す
	 * @param str
	 * @return
	 */
	private int[] stringToIntArray(String str) {
		String[] strArray;
		// strを配列に直す
		if (str.contains(",")) {
			strArray = str.split(",");
		} else {
			strArray = new String[1];
			strArray[0] = str;
		}
		// 不正な締め日が入力されていないか確認
		for (int i = 0; i < strArray.length; i++) {
			// 数字以外が入力されていないか確認
			if (!checkIsNumber(strArray[i])) {
				System.out.println("締め日のフォーマットが不正です。");
				System.exit(0);
			}
		}
		// int型の配列に直す
		int[] intArray = Stream.of(strArray).mapToInt(Integer::parseInt).toArray();
		// 不正な締め日が入力されていないか確認
		// 同じ数字や、0以下32以上の数字が含まれていないか確認
		if (!checkIsValidNumber(intArray)) {
			System.out.println("締め日のフォーマットが不正です。");
			System.exit(0);
		}
		Arrays.sort(intArray);
		return intArray;
	}

	/**
	 * 0以下32以上の数字や、同じ数字が含まれていないか確認
	 * @param intArray
	 * @return
	 */
	private boolean checkIsValidNumber(int[] intArray) {
		boolean[] from1To31Array = new boolean[31];
		for (int i = 0; i < intArray.length; i++) {
			if (!(1 <= intArray[i] && intArray[i] <= 31)) {
				return false;
			}
			if (!from1To31Array[intArray[i] - 1]) {
				from1To31Array[intArray[i] - 1] = true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 *  数字か数字以外が含まれるか判定
	 * @param string
	 * @return
	 */
	private boolean checkIsNumber(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 開始日と締め日の配列を生成
	 * @param indexOfLastClosingDate
	 * @param intClosingDayArray
	 * @param isBackOneMonth
	 * @return
	 */
	public List<String> createSixMonthsOpeningAndClosingDateList(int[] intClosingDayArray) {
		// 現在日時
		Calendar tmpCalendar = Calendar.getInstance();
		int tmpCurrentDate = tmpCalendar.get(Calendar.DATE);

		// 12月分の月末が入った配列を生成
		int[] intEndOfMonthsArray = new int[12];
		for (int i = 0; i < 12; i++) {
			switch (i) {
			case 0:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				intEndOfMonthsArray[i] = 31;
				break;
			case 3:
			case 5:
			case 8:
			case 10:
				intEndOfMonthsArray[i] = 30;
				break;
			case 1:
				intEndOfMonthsArray[i] = 28;
				break;
			}
		}

		// 直近の締め日を取得
		int lastClosingDate = 0;
		int indexOfLastClosingDate = 0;
		boolean isBackOneMonth = false;
		boolean isClosingDateFound = false;
		while (!isClosingDateFound) {
			for (int i = 0; i < intClosingDayArray.length; i++) {
				if (i == 0 && tmpCurrentDate < intClosingDayArray[i]) {
					// 締め日が、配列の最後（締め日は先月）
					lastClosingDate = intClosingDayArray[intClosingDayArray.length - 1];
					indexOfLastClosingDate = intClosingDayArray.length - 1;
					isBackOneMonth = true;
					isClosingDateFound = true;
				} else if (!isClosingDateFound && tmpCurrentDate < intClosingDayArray[i]) {
					// 締め日が、配列の最後以外のどれか
					lastClosingDate = intClosingDayArray[i - 1];
					indexOfLastClosingDate = i - 1;
					isClosingDateFound = true;
				} else if (!isClosingDateFound && i == intClosingDayArray.length - 1) {
					// 締め日が、配列の最後（締め日は当月）
					lastClosingDate = intClosingDayArray[i];
					indexOfLastClosingDate = i;
					isClosingDateFound = true;
				}
			}
		}

		// 開始日と締め日を配列に格納
		List<String> periods = new ArrayList<>();
		for (int month = 0; month < 13; month++) {
			// 現在日時を取得
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, - month);
			int lastClosingMonth = 0;
			if (isBackOneMonth) {
				// 直近の締め日が先月だった場合は、1ヵ月前からスタート
				month += 1;
				calendar.add(Calendar.MONTH, - month);
				lastClosingMonth = calendar.get(Calendar.MONTH) + 1;
				isBackOneMonth = false;
			} else {
				// 当月からスタート
				lastClosingMonth = calendar.get(Calendar.MONTH) + 1;
			}
			int currentYear = calendar.get(Calendar.YEAR);

			// intClosingDayArrayの中の締め日のindexを取得
			int indexOfClosingDate = 0;
			// 初月は直近の締め日のindexを取得して、それ以外は配列の最後の締め日のindexを取得
			indexOfClosingDate = (month == 0) ? indexOfLastClosingDate : intClosingDayArray.length - 1;

			// 開始日と締め日を取得
			// start == 締め日配列を取得し始める位置
			for (int start = indexOfClosingDate; 0 <= start; start--) {
				int closingDate = (start == intClosingDayArray.length - 1) ? getClosingDate(intClosingDayArray, start, lastClosingMonth, currentYear, intEndOfMonthsArray) : intClosingDayArray[start];
				periods.add(getOpeningDate(intClosingDayArray, start, calendar, intEndOfMonthsArray) + " - " + currentYear + "/" + lastClosingMonth + "/" + closingDate);
				System.out.println(getOpeningDate(intClosingDayArray, start, calendar, intEndOfMonthsArray) + " - " + currentYear + "/" + lastClosingMonth + "/" + closingDate);
			}
		}
		return periods;
	}

	/**
	 * 締め日を取得
	 * @param intClosingDayArray
	 * @param i
	 * @param currentMonth
	 * @param currentYear
	 * @param intEndOfMonthsArray
	 * @return
	 */
	private int getClosingDate(int[] intClosingDayArray, int start, int currentMonth, int currentYear, int[] intEndOfMonthsArray) {
		// 閏年の場合は2月28日を29日に変更
		if (currentYear % 4 == 0) {
			intEndOfMonthsArray[1] = 29;
		}
		// 締め日が月末だった場合に、それぞれの月の月末に応じて表示する締め日を変える
		int endOfIndexClosingDate = 0;
		endOfIndexClosingDate = (intEndOfMonthsArray[currentMonth - 1] < intClosingDayArray[start]) ? intEndOfMonthsArray[currentMonth - 1] : intClosingDayArray[start];

		return endOfIndexClosingDate;
	}

	/**
	 *  開始日を取得
	 * @param intClosingDayArray
	 * @param i
	 * @param calendar
	 * @param intEndOfMonthsArray
	 * @return
	 */
	private String getOpeningDate(int[] intClosingDayArray, int start, Calendar calendar, int[] intEndOfMonthsArray) {
		int openingDay = intClosingDayArray[intClosingDayArray.length - 1];
		int lastMonth = calendar.get(Calendar.MONTH) + 1;
		int lastYear = calendar.get(Calendar.YEAR);
		// 閏年の場合は2月28日を29日に変更
		if (lastYear % 4 == 0) {
			intEndOfMonthsArray[1] = 29;
		}
		String openingDate = null;
		if (start == 0) {
			// 月を跨ぐ場合の開始日を取得
			lastMonth = (lastMonth == 1) ? 12 : lastMonth - 1;
			lastYear = (lastMonth == 12) ? lastYear - 1 : lastYear;
			if (intClosingDayArray[intClosingDayArray.length - 1] > 27) {
				openingDay = (intEndOfMonthsArray[lastMonth - 1] <= intClosingDayArray[intClosingDayArray.length - 1]) ? 1 : intClosingDayArray[intClosingDayArray.length - 1] + 1;
			} else {
				openingDay += 1;
			}
			if (openingDay == 1 && lastMonth == 12) {
				lastMonth = 1;
				lastYear += 1;
			} else if (openingDay == 1) {
				lastMonth += 1;
			}
			openingDate = lastYear + "/" + lastMonth + "/" + openingDay;
		} else {
			openingDay = intClosingDayArray[start - 1] + 1;
			openingDate = lastYear + "/" + lastMonth + "/" + openingDay;
		}
		return openingDate;
	}

}
