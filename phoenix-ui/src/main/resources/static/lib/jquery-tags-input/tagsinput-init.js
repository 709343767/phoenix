//tag input
$(function () {
    // 收件人邮箱
    $('textarea[name="alarmMailEmills"]').tagsInput({
        width: 'auto',
        'delimiter': ';',
        onChange: function (elem, elem_tags) {
            $('.tag').css({'background-color': '#D2D2D2', 'border-color': '#D2D2D2'});
        }
    });
    // 收件人号码
    $('textarea[name="alarmSmsPhoneNumbers"]').tagsInput({
        width: 'auto',
        'delimiter': ';',
        onChange: function (elem, elem_tags) {
            $('.tag').css({'background-color': '#D2D2D2', 'border-color': '#D2D2D2'});
        }
    });
});
