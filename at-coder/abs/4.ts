function Main(input: string | number) {
  const tmpArr = input.split("\n");
  let strArr = tmpArr[1].split(" ").map(item => parseInt(item, 10))
  let divisionTimes: number = 0
  
  const arrLength = parseInt(tmpArr[0], 10)
  let filteredArr = strArr.filter(item => item % 2 === 0)
  while (arrLength === filteredArr.length) {
    divisionTimes++;
    filteredArr = filteredArr.map(item => item / 2)
    filteredArr = filteredArr.filter(item => item % 2 === 0)
  }
  console.log(divisionTimes)
}
//*この行以降は編集しないでください（標準入出力から一度に読み込み、Mainを呼び出します）
Main(require("fs").readFileSync("/dev/stdin", "utf8"));
