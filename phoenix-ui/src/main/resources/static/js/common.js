var webConst = {
    /**
     * 成功
     */
    SUCCESS: 'success',
    /**
     * 失败
     */
    FAIL: 'fail',
    /**
     * 已经存在
     */
    EXIST: 'exist',
    /**
     * 校验失败
     */
    VERIFY_FAIL: 'verifyFail',
    /**
     * 刷新失败
     */
    REFRESH_FAIL: 'refreshFail',
    /**
     * 必选项（必填项）为空
     */
    REQUIRED_IS_NULL: 'requiredIsNull',
    /**
     * 数据完整性冲突
     */
    DATA_INTEGRITY_VIOLATION: 'dataIntegrityViolation'
};

/**
 * 合并指定单元格
 * @param $ query
 * @param tableId 表格的id，id前面要加#，例如：#list-table
 * @param res 结果集
 * @param columsIndex 需要合并的列索引值（数组）
 * @param columsName 需要合并的列名称（数组）
 */
var merge = function ($, res, tableId, columsIndex, columsName) {
    var data = res.data;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(tableId + ",.layui-table-body>.layui-table").find("tr");//所有行
        for (var i = 1; i < res.data.length; i++) { //这里循环表格当前的数据
            var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
            var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
            if (data[i][columsName[k]] === data[i - 1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
                mark += 1;
                tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                    $(this).attr("rowspan", mark);
                });
                tdCurArr.each(function () {//当前行隐藏
                    $(this).css("display", "none");
                });
            } else {
                mergeIndex = i;
                mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
            }
        }
        mergeIndex = 0;
        mark = 1;
    }
};

/**
 * 判断字符串是否为空
 * @param o 字符串对象
 * @returns {boolean}
 */
var isEmpty = function (o) {
    return typeof o === 'undefined' || o == null || o === '';
};

/**
 * js获取UUID
 * @returns {string}
 */
var getUUID = function () {
    let s = [];
    let hexDigits = '0123456789abcdef';
    for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = '4';
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23] = '-';
    return s.join('');
};

/**
 * 内存字节转化
 * @param num 字节
 * @returns {string|*}
 */
var convertSize = function (num) {
    if (num === 0) return '0 B';
    var k = 1024; //设定基础容量大小
    var sizeStr = ['B', 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y']; //容量单位
    var i = 0; //单位下标和次幂
    for (var l = 0; l < 8; l++) {   //因为只有8个单位所以循环八次
        if (num / Math.pow(k, l) < 1) { //判断传入数值 除以 基础大小的次幂 是否小于1，这里小于1 就代表已经当前下标的单位已经不合适了所以跳出循环
            break; //小于1跳出循环
        }
        i = l; //不小于1的话这个单位就合适或者还要大于这个单位 接着循环
    }
    // 例： 900 / Math.pow(1024, 0)  1024的0 次幂 是1 所以只要输入的不小于1 这个最小单位就成立了；
    //     900 / Math.pow(1024, 1)  1024的1次幂 是1024  900/1024 < 1 所以跳出循环 下边的 i = l；就不会执行  所以 i = 0； sizeStr[0] = 'B';
    //     以此类推 直到循环结束 或 条件成立
    return (num / Math.pow(k, i)).toFixed(2) + ' ' + sizeStr[i];  //循环结束 或 条件成立 返回字符
};