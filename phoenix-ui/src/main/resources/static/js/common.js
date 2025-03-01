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
    DATA_INTEGRITY_VIOLATION: 'dataIntegrityViolation',
    /**
     * 未获取
     */
    NOT_OBTAINED: 'notObtained'
};

var webConstCn = {
    /**
     * 未获取
     */
    NOT_OBTAINED_CN: '未获取'
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
 * 判断字符串是否不为空
 * @param o 字符串对象
 * @returns {boolean}
 */
var isNotEmpty = function (o) {
    return !isEmpty(o);
};

/**
 * 判断map对象是否为空
 * @param map map对象
 * @returns {boolean}
 */
var isEmptyMap = function (map) {
    return map.size === 0;
};

/**
 * 求第一个 Map 相对于第二个 Map 的差集
 * 上述代码中，mapDifference 函数接受两个 Map 对象作为参数，并返回一个包含两个差集 Map 的对象。首先，通过 Array.from 方法将 Map 的键转换为数组，
 * 然后使用 filter 方法找出两个 Map 中不相同的键。最后，通过这些键创建新的 Map 对象，并返回结果。
 * 请注意，该代码仅比较了键，如果你想对值进行比较，可以在过滤的步骤中加入相应的条件判断。
 * @param map1 第一个map对象
 * @param map2 第二个map对象
 * @returns {Map<unknown, *>}
 */
var mapDifference = function (map1, map2) {
    const keys1 = Array.from(map1.keys());
    const keys2 = Array.from(map2.keys());
    // 求第一个 Map 相对于第二个 Map 的差集
    const difference = keys1.filter(key => !keys2.includes(key));
    return new Map(difference.map(key => [key, map1.get(key)]));
};

/**
 * 判断是否为json对象
 * @param obj
 * @returns {boolean}
 */
var isJson = function (obj) {
    return typeof (obj) == 'object'
        && Object.prototype.toString.call(obj).toLowerCase() === '[object object]'
        && !obj.length;
};

/**
 * 已知中心点和半径,求圆上任意一点
 * @param x 圆心X
 * @param y 圆心Y
 * @param r 半径
 * @returns {[*, *]}
 */
var randCirclePoint = function (x, y, r) {
    var radius = 2 * Math.PI * Math.random();
    var targetX = x + r * Math.cos(radius);
    var targetY = y + r * Math.sin(radius);
    return [targetX, targetY];
};

/**
 * 获取页面元素中心点
 * @param $this jQuery选择器，如：$('#id')
 * @returns {[*, *]}
 */
var elementCenterPoint = function ($this) {
    var offset = $this.offset();
    var width = $this.width();
    var height = $this.height();
    var centerX = offset.left + width / 2;
    var centerY = offset.top + height / 2;
    return [centerX, centerY];
};

/**
 * 根据数组中对象的某个属性值进行去重
 * @param arr 数组
 * @param key  对象的某个属性
 * @returns {any[]}
 */
var uniqueArr = function (arr, key) {
    var map = new Map();
    arr.forEach((item) => {
        if (!map.has(item[key])) {
            map.set(item[key], item)
        }
    });
    return [...map.values()];
};

/**
 * 从对象数组中返回对象某个属性值组成的数组
 * @param objArr 对象数组
 * @param attribute 属性名
 * @param removeDuplicates 是否去重
 * @returns {any[]}
 */
var objectArr2AttributeArr = function (objArr, attribute, removeDuplicates) {
    var newArr = objArr.map((obj) => {
        return obj[attribute];
    });
    // 去重
    if (removeDuplicates) {
        newArr = Array.from(new Set(newArr));
    }
    return newArr;
};

/**
 * 求两基础数据类型数组交集
 * @param arr1 数组1
 * @param arr2 数组2
 */
var arrIntersect = function (arr1, arr2) {
    return Array.from(new Set(arr1.filter(item => {
        return arr2.includes(item);
    })));
};

/**
 * 根据目标对象的某个key判断对象数组中是否存在某个目标对象
 * @param arr 对象数组
 * @param targetObj 目标对象
 * @param targetObjKey 目标对象中用于判断的key
 * @returns {*}
 */
var arrExistingObj = function (arr, targetObj, targetObjKey) {
    return arr.some(obj => {
        // 这里假设对象的判断条件是判断 targetObjKey 的值
        return obj[targetObjKey] === targetObj[targetObjKey];
    });
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
    var sizeStr = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']; //容量单位
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

/**
 * 时间毫秒转换成天小时分钟秒毫秒的字符串
 * @param milli 毫秒
 * @param type 单位类型：en、cn，分别代表：英文格式、中文格式，可以省略，默认：en
 * @returns {string}
 */
var formatMillisecond = function (milli, type) {
    //秒
    var second = 0;
    // 分
    var minute = 0;
    // 小时
    var hour = 0;
    // 天
    var day = 0;
    // 英文单位
    var en = ['ms', 's', 'min', 'h', 'd'];
    // 中文单位
    var cn = ['毫秒', '秒', '分', '小时', '天'];
    if (milli >= 1000) {
        second = parseInt(milli / 1000);
        milli = milli % 1000;
        if (second > 1) {
            minute = parseInt(second / 60);
            second = second % 60;
            if (minute > 1) {
                hour = parseInt(minute / 60);
                minute = minute % 60;
                if (hour > 1) {
                    day = parseInt(hour / 24);
                    hour = hour % 24;
                }
            }
        }
    }
    if (day >= 1) {
        return type === 'cn' ?
            day + cn[4] + ((hour + minute + second + milli) > 0 ? hour + cn[3] + ((minute + second + milli) > 0 ? minute + cn[2] + ((second + milli) > 0 ? second + cn[1] + (milli > 0 ? milli + cn[0] : '') : '') : '') : '') :
            day + en[4] + ((hour + minute + second + milli) > 0 ? hour + en[3] + ((minute + second + milli) > 0 ? minute + en[2] + ((second + milli) > 0 ? second + en[1] + (milli > 0 ? milli + en[0] : '') : '') : '') : '');
    }
    if (hour >= 1) {
        return type === 'cn' ?
            hour + cn[3] + ((minute + second + milli) > 0 ? minute + cn[2] + ((second + milli) > 0 ? second + cn[1] + (milli > 0 ? milli + cn[0] : '') : '') : '') :
            hour + en[3] + ((minute + second + milli) > 0 ? minute + en[2] + ((second + milli) > 0 ? second + en[1] + (milli > 0 ? milli + en[0] : '') : '') : '');
    }
    if (minute >= 1) {
        return type === 'cn' ?
            minute + cn[2] + ((second + milli) > 0 ? second + cn[1] + (milli > 0 ? milli + cn[0] : '') : '') :
            minute + en[2] + ((second + milli) > 0 ? second + en[1] + (milli > 0 ? milli + en[0] : '') : '');
    }
    if (second >= 1) {
        return type === 'cn' ?
            second + cn[1] + (milli > 0 ? milli + cn[0] : '') :
            second + en[1] + (milli > 0 ? milli + en[0] : '');
    }
    return type === 'cn' ?
        milli + cn[0] :
        milli + en[0];
};

/**
 * 清空所有字段的值
 * @param fields 包含所有字段的JSON对象
 * @returns {*}
 */
var clearFields = function (fields) {
    if (isNotEmpty(fields)) {
        for (var key in fields) {
            // 确保你只操作对象自身的属性，而不是继承自原型链上的属性
            if (fields.hasOwnProperty(key)) {
                fields[key] = '';
            }
        }
    }
    return fields;
};