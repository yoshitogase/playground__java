// inputに入力データ全体が入る
function Main(input: string) {
	// 1行目がinput[0], 2行目がinput[1], …に入る
	let strArr: string[] = input.split("\n");
	let tmpArr: string[] = strArr[1].split(" ");
	//文字列から10進数に変換するときはparseIntを使います
	var a = parseInt(strArr[0], 10);
	var b = parseInt(tmpArr[0], 10);
	var c = parseInt(tmpArr[1], 10);
	var string = strArr[2];
	//出力
	console.log('%d %s', a+b+c, string);
}
//*この行以降は編集しないでください（標準入出力から一度に読み込み、Mainを呼び出します）
Main(require("fs").readFileSync("/dev/stdin", "utf8"));
