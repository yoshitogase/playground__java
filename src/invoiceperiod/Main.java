package invoiceperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

public class Main {
	
	public static void main(String[] args) {
		
		/**
		 *  前給の時のinvoiceを作成する時の処理を再現
		 *  
		 *  テスト
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
		String closingDay = "5,15,31"; // 毎月末を指定する場合は31を入れる
		// 締め日を配列化（締め日が1つの時とそれ以外の時）
		String[] strClosingDayArray;
		if (closingDay.contains(",")) {
			strClosingDayArray = closingDay.split(",");
		} else {
			strClosingDayArray = new String[1];
			strClosingDayArray[0] = closingDay;
		}
		int[] intClosingDayArray = Stream.of(strClosingDayArray) .mapToInt(Integer::parseInt).toArray();
		Arrays.sort(intClosingDayArray);
		
		// 過去6ヶ月間の開始日と締め日のペアを生成する
		System.out.println(returnPeriods(intClosingDayArray));
	}
	
	private static List<String> returnPeriods(int[] intClosingDayArray) {
		// 現在日時
		Calendar tmpCalendar = Calendar.getInstance();
		int tmpCurrentDate = tmpCalendar.get(Calendar.DATE);
		
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
		// 開始日と締め日生成して、リストに入れる
		System.out.println(lastClosingDate + " " + indexOfLastClosingDate); //消す
		return createSixMonthsOpeningAndClosingDateList(indexOfLastClosingDate, intClosingDayArray, isBackOneMonth);
	}
	
	/**
	 * 開始日と締め日の配列を生成
	 * @param indexOfLastClosingDate
	 * @param intClosingDayArray
	 * @param isBackOneMonth
	 * @return
	 */
	private static List<String> createSixMonthsOpeningAndClosingDateList(int indexOfLastClosingDate, int[] intClosingDayArray, boolean isBackOneMonth) {
		String endOfMonths = "31,28,31,30,31,30,31,31,30,31,30,31";
		String[] endOfMonthsArray;
		endOfMonthsArray = endOfMonths.split(",");
		int[] intEndOfMonthsArray = Stream.of(endOfMonthsArray) .mapToInt(Integer::parseInt).toArray();
		List<String> periods = new ArrayList<>();
		for (int month = 0; month < 6; month++) { 
			// 現在日時を取得
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, - month);
			int currentMonth = 0;
			if (isBackOneMonth) {
				// 直近の締め日が先月だった場合は、1ヵ月前からスタート
				month += 1;
				calendar.add(Calendar.MONTH, - month);
				currentMonth = calendar.get(Calendar.MONTH) + 1;
				isBackOneMonth = false;
			} else {
				// 当月からスタート
				currentMonth = calendar.get(Calendar.MONTH) + 1;
			}
			int currentYear = calendar.get(Calendar.YEAR);
			
			// intClosingDayArrayの中の締め日のindexを取得
			int indexOfClosingDate = 0;
			indexOfClosingDate = (month == 0) ? indexOfLastClosingDate : intClosingDayArray.length - 1;
			
			// 開始日と締め日を取得
			for (int start = indexOfClosingDate; 0 <= start && start < intClosingDayArray.length; start--) {
				int closingDate = (start == intClosingDayArray.length - 1) ? getClosingDate(intClosingDayArray, start, currentMonth, currentYear, intEndOfMonthsArray) : intClosingDayArray[start];
				periods.add(getOpeningDate(intClosingDayArray, start, calendar, intEndOfMonthsArray) + " - " + currentYear + "/" + currentMonth + "/" + closingDate);
				System.out.println(getOpeningDate(intClosingDayArray, start, calendar, intEndOfMonthsArray) + " - " + currentYear + "/" + currentMonth + "/" + closingDate);
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
	private static int getClosingDate(int[] intClosingDayArray, int i, int currentMonth, int currentYear, int[] intEndOfMonthsArray) {
		// 閏年の場合は2月28日を29日に変更
		if (currentYear % 4 == 0) {
			intEndOfMonthsArray[1] = 29;
		}
		int endOfIndexClosingDate = 0;
		endOfIndexClosingDate = (intEndOfMonthsArray[currentMonth - 1] < intClosingDayArray[i]) ? intEndOfMonthsArray[currentMonth - 1] : intClosingDayArray[i];
		
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
	private static String getOpeningDate(int[] intClosingDayArray, int i, Calendar calendar, int[] intEndOfMonthsArray) {
		int openingDay = intClosingDayArray[intClosingDayArray.length - 1];
		int lastMonth = calendar.get(Calendar.MONTH) + 1;
		int lastYear = calendar.get(Calendar.YEAR);
		// 閏年の場合は2月28日を29日に変更
		if (lastYear % 4 == 0) {
			intEndOfMonthsArray[1] = 29;
		}
		String openingDate = null;
		if (i == 0) {
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
			} else if (openingDay == 1) {
				lastMonth += 1;
			}
			openingDate = lastYear + "/" + lastMonth + "/" + openingDay;
		} else {
			openingDay = intClosingDayArray[i - 1] + 1;
			openingDate = lastYear + "/" + lastMonth + "/" + openingDay;
		}
		return openingDate;
	}
	
}
