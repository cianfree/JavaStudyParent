/**
 * 找出元素 item 在给定数组 arr 中的位置
 * @param array
 * @param item
 * @return number item，则返回元素在数组中的位置，否则返回 -1
 */
function indexOf(arr, item) {
    if (!arr || arr.length < 1 || !item) return -1;
    for (var i=0; i<arr.length; ++i) {
        if (arr[i] === item) return i;
    }
    return -1;
}

/**
 * 移除数组 arr 中的所有值与 item 相等的元素，直接在给定的 arr 数组上进行操作，并将结果返回
 * @param arr
 * @param item
 * @returns {Array}
 */
function removeWithoutCopy(arr, item) {
    if (!arr || arr.length < 1 || !item) return arr;
    for (var i = arr.length - 1; i >=0 ; --i) {
        if (arr[i] === item) {
            arr.splice(i, 1);
        }
    }
    return arr;
}

function duplicates(arr) {
    var newArr = [];
    for (var i=0; i<arr.length; ++i) {
        if (arr.lastIndexOf(arr[i]) !== i && newArr.indexOf(arr[i]) == -1) newArr.push(arr[i]);
    }
    return newArr;
}

function duplicatesByMap(arr) {
    var map = {};
    for (var i=0; i<arr.length; ++i) {
        var n = map[arr[i]];
        if (n) {
            map[arr[i]] = n + 1;
        } else {
            map[arr[i]] = 1;
        }
    }
    var newArr = [];
    for (var pro in map) {
        if (map[pro] && map[pro] > 1) newArr.push(pro);
    }
    return newArr;
}

/**
 * 给定字符串 str，检查其是否包含连续重复的字母（a-zA-Z），包含返回 true，否则返回 false
 * @param str
 */
function containsRepeatingLetter(str) {
    //
}