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

$(function() {
	// 페이지 로드 시 데이터를 가져오는 AJAX 요청
	loadData();

	// dateSelect 요소의 변경 이벤트 핸들러
	$('#dateSelect').change(function() {
		var selectedDate = $(this).val(); // 선택된 날짜 값을 가져옵니다.

		// 데이터 필터링 및 업데이트
		filterData(selectedDate);
	});

	// 데이터 로딩 함수
	function loadData() {
		$.ajax({
			url: '/histories_api',
			success: function(data) {
				// data 변수에 전체 데이터가 들어옵니다.
				// 필요한 경우 이 데이터를 적절하게 활용하세요.
				updateTable(data);
			}
		});
	}


	// 데이터 필터링 함수
	function filterData(selectedDate) {
		$.ajax({
			url: '/histories_api',
			success: function(data) {
				// 선택된 날짜와 일치하는 데이터만 필터링하여 표시합니다.
				var filteredData = data.filter(function(item) {
					return item.created.startsWith(selectedDate);
				});

				// 데이터 업데이트
				updateTable(filteredData);
			}
		});
	}

	// 테이블 업데이트 함수
	function updateTable(data) {
		// 테이블 초기화
		$('.list tbody').empty();

		// 데이터 표시
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

	$(function() {
		// 페이지 로드 시 날짜 목록을 가져와서 드롭다운 목록을 채웁니다.
		$.ajax({
			url: '/dates',
			success: function(data) {
				// 날짜 목록을 받아서 드롭다운 목록에 옵션으로 추가합니다.
				var dateSelect = $('#dateSelect');
				for (var i = 0; i < data.length; i++) {
					var date = data[i];
					dateSelect.append('<option value="' + date + '">' + date + '</option>');
				}

				// 드롭다운 목록의 변경 이벤트 핸들러
				dateSelect.change(function() {
					var selectedDate = $(this).val(); // 선택된 날짜 값을 가져옵니다.
					filterData(selectedDate); // 선택된 날짜로 데이터 필터링
				});
			}
		});
	});

	// 배경색의 밝기를 기준으로 글자색을 선택하는 함수
	function getTextColor(bgColor) {
		// 색상 코드를 RGB로 변환
		var hex = bgColor.replace(/^#/, '');
		var red = parseInt(hex.substring(0, 2), 16);
		var green = parseInt(hex.substring(2, 4), 16);
		var blue = parseInt(hex.substring(4, 6), 16);

		// 밝기 계산 (YIQ 방식을 사용)
		var brightness = (red * 299 + green * 587 + blue * 114) / 1000;

		// 밝기가 128 미만인 경우 흰색 반환, 그렇지 않으면 검은색 반환
		return brightness < 128 ? 'white' : 'black';
	}

	// 데이터 필터링 함수
	function filterData(selectedDate) {
		$.ajax({
			url: '/histories_api',
			success: function(data) {
				// 선택된 날짜와 일치하는 데이터만 필터링하여 표시합니다.
				var filteredData = data.filter(function(item) {
					return item.created.startsWith(selectedDate);
				});

				// 데이터 업데이트
				updateTable(filteredData);
			}
		});
	}

	// 테이블 업데이트 함수
	function updateTable(data) {
		// 테이블 초기화
		$('.list tbody').empty();

		// 데이터 표시
		for (let i = 0; i < data.length; i++) {
			let row = data[i];
			let tr = $("<tr></tr>");
			tr.append('<td>' + row['idx'] + '</td>');
			tr.append('<td>' + row['code'] + '</td>');
			tr.append('<td>' + row['created'] + '</td>');
			tr.append('<td><input type="button" name="" value="삭제" onClick="deletes (' + row['idx'] + ')"></td>');

			// 배경색 설정
			tr.css('background-color', row['code']);

			// 글자색 설정
			var textColor = getTextColor(row['code']);
			tr.css('color', textColor);

			$('.list tbody').append(tr);
		}
	}
});
