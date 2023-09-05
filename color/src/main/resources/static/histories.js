$(function() {
    // 페이지 로드 시 데이터를 가져오는 AJAX 요청
    $.ajax({
        url: '/histories_api',
        success: function(data) {
            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                let tr = $("<tr></tr>");
                tr.append('<td>' + row['idx'] + '</td>');
                tr.append('<td>' + row['code'] + '</td>');
                tr.append('<td>' + row['created'] + '</td>');
                tr.append('<td><input type="button" name="" value="삭제" onClick="deletes (' + row['idx'] + ')"></td>');
                $('.list tbody').append(tr);
                tr.css('background-color', row['code']);
            }
        }
    });
});

    function deletes(idx) {
        $.ajax({
            url: `/deletePost?idx=${idx}`,
            method: 'POST',
            dataType: 'text',
            success: function(response) {
                console.log(idx);
                if (response.includes("삭제가 완료되었습니다.")) {
                    alert("삭제 성공");
                    $(`#post_${idx}`).remove();
                } else {
                    alert("삭제 실패");
                }
            },
            error: function() {
                alert('오류 발생');
            }
        });
    }