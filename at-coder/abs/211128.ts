// function Main(input: string) {
//   let oneCounter: number = 0;
//   for (let index = 0; index < input.length; index++) {
//     const pickedStr = input.substring(index,1)
//     if (pickedStr === '1') oneCounter++
//   }
// 	// var b = parseInt(strArr[1], 10);
//   console.log(oneCounter)
// }
// //*この行以降は編集しないでください（標準入出力から一度に読み込み、Mainを呼び出します）
// Main(require("fs").readFileSync("/dev/stdin", "utf8"));
function Main(input: string) {
  let oneCounter: number = 0;
  for (let index = 0; index < input.length; index++) {
    const pickedStr = input.substring(index,index+1)
    console.log(pickedStr === '1')
    if (pickedStr === '1') oneCounter++
  }
	// var b = parseInt(strArr[1], 10);
  console.log(oneCounter)
}
//*この行以降は編集しないでください（標準入出力から一度に読み込み、Mainを呼び出します）
Main(require("fs").readFileSync("/dev/stdin", "utf8"));
