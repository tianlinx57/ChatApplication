$(document).ready(function() {
    var table = $('#userTable');
    var rowsPerPage = 5; // Number of rows to display per page

    // Calculate the number of pages
    var totalPages = Math.ceil(table.find('tbody tr').length / rowsPerPage);

    // Generate the pagination links
    for (var i = 1; i <= totalPages; i++) {
        var paginationLink = '<li class="page-item"><a class="page-link" href="#">' + i + '</a></li>';
        $('#paginationContainer .pagination').append(paginationLink);
    }

    // Show the first page initially
    showPage(1);

    // Handle pagination link clicks
    $('#paginationContainer .pagination').on('click', 'a', function(e) {
        e.preventDefault();
        var page = $(this).text();
        showPage(page);
    });

    // Function to show the specified page
    function showPage(page) {
        var startIndex = (page - 1) * rowsPerPage;
        var endIndex = startIndex + rowsPerPage;
        table.find('tbody tr').hide();
        table.find('tbody tr').slice(startIndex, endIndex).show();

        // Highlight the active pagination link
        $('#paginationContainer .pagination li').removeClass('active');
        $('#paginationContainer .pagination li:nth-child(' + (parseInt(page) + 1) + ')').addClass('active');
    }
});