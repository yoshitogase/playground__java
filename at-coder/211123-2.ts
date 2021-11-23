function Main(input: string) {
	let strArr: string[] = input.split(" ");
	var a = parseInt(strArr[0], 10);
	var b = parseInt(strArr[1], 10);
  if ((a * b) % 2 === 0) {
    console.log('Even')
  } else {
    console.log('Odd')
  }
}
//*この行以降は編集しないでください（標準入出力から一度に読み込み、Mainを呼び出します）
Main(require("fs").readFileSync("/dev/stdin", "utf8"));
