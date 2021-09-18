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
    REQUIRED_IS_NULL: 'requiredIsNull'
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