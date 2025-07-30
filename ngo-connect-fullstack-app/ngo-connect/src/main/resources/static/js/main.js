// NGO Connect - Main JavaScript

$(document).ready(function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);

    // Smooth scrolling for anchor links
    $('a[href^="#"]').on('click', function(event) {
        var target = $(this.getAttribute('href'));
        if( target.length ) {
            event.preventDefault();
            $('html, body').stop().animate({
                scrollTop: target.offset().top - 100
            }, 1000);
        }
    });

    // Dashboard sidebar toggle for mobile
    $('.dashboard-mobile-toggle').on('click', function() {
        $('.dashboard-sidebar').toggleClass('show');
    });

    // Close sidebar when clicking outside on mobile
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.dashboard-sidebar, .dashboard-mobile-toggle').length) {
            $('.dashboard-sidebar').removeClass('show');
        }
    });

    // Donation amount selection
    $('.amount-btn').on('click', function() {
        $('.amount-btn').removeClass('active');
        $(this).addClass('active');
        $('#donationAmount').val($(this).data('amount'));
    });

    // Custom donation amount
    $('#donationAmount').on('input', function() {
        $('.amount-btn').removeClass('active');
    });

    // Form validation
    $('form').on('submit', function(event) {
        var form = this;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    });

    // Password confirmation validation
    $('#confirmPassword').on('input', function() {
        var password = $('#password').val();
        var confirmPassword = $(this).val();
        
        if (password !== confirmPassword) {
            this.setCustomValidity('Passwords do not match');
        } else {
            this.setCustomValidity('');
        }
    });

    // File upload preview
    $('input[type="file"]').on('change', function() {
        var file = this.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var preview = $(this).siblings('.file-preview');
                if (preview.length) {
                    preview.attr('src', e.target.result).show();
                }
            }.bind(this);
            reader.readAsDataURL(file);
        }
    });

    // Search functionality
    $('#searchInput').on('input', function() {
        var searchTerm = $(this).val().toLowerCase();
        $('.searchable-item').each(function() {
            var text = $(this).text().toLowerCase();
            if (text.includes(searchTerm)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    // Filter functionality
    $('.filter-btn').on('click', function() {
        var filter = $(this).data('filter');
        $('.filter-btn').removeClass('active');
        $(this).addClass('active');
        
        if (filter === 'all') {
            $('.filterable-item').show();
        } else {
            $('.filterable-item').hide();
            $('.filterable-item[data-category="' + filter + '"]').show();
        }
    });

    // Load more functionality
    $('.load-more-btn').on('click', function() {
        var button = $(this);
        var container = button.data('container');
        var url = button.data('url');
        var page = parseInt(button.data('page')) + 1;
        
        button.prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> Loading...');
        
        $.get(url + '?page=' + page, function(data) {
            $(container).append(data);
            button.data('page', page);
            button.prop('disabled', false).html('Load More');
        }).fail(function() {
            button.prop('disabled', false).html('Load More');
            showAlert('Error loading more items', 'danger');
        });
    });

    // AJAX form submission
    $('.ajax-form').on('submit', function(event) {
        event.preventDefault();
        
        var form = $(this);
        var url = form.attr('action');
        var method = form.attr('method') || 'POST';
        var data = form.serialize();
        
        // Show loading state
        var submitBtn = form.find('button[type="submit"]');
        var originalText = submitBtn.html();
        submitBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> Processing...');
        
        $.ajax({
            url: url,
            method: method,
            data: data,
            success: function(response) {
                if (response.success) {
                    showAlert(response.message, 'success');
                    if (response.redirect) {
                        setTimeout(function() {
                            window.location.href = response.redirect;
                        }, 1500);
                    }
                } else {
                    showAlert(response.message, 'danger');
                }
            },
            error: function() {
                showAlert('An error occurred. Please try again.', 'danger');
            },
            complete: function() {
                submitBtn.prop('disabled', false).html(originalText);
            }
        });
    });

    // Progress bar animation
    $('.progress-bar').each(function() {
        var progress = $(this).data('progress');
        $(this).animate({
            width: progress + '%'
        }, 1000);
    });

    // Counter animation
    $('.counter').each(function() {
        var $this = $(this);
        var countTo = $this.attr('data-count');
        
        $({ countNum: $this.text() }).animate({
            countNum: countTo
        }, {
            duration: 2000,
            easing: 'linear',
            step: function() {
                $this.text(Math.floor(this.countNum));
            },
            complete: function() {
                $this.text(this.countNum);
            }
        });
    });

    // Lazy loading for images
    if ('IntersectionObserver' in window) {
        var imageObserver = new IntersectionObserver(function(entries, observer) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    var img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        document.querySelectorAll('img[data-src]').forEach(function(img) {
            imageObserver.observe(img);
        });
    }

    // Copy to clipboard functionality
    $('.copy-btn').on('click', function() {
        var text = $(this).data('copy');
        navigator.clipboard.writeText(text).then(function() {
            showAlert('Copied to clipboard!', 'success');
        });
    });

    // Share functionality
    $('.share-btn').on('click', function() {
        var url = $(this).data('url') || window.location.href;
        var title = $(this).data('title') || document.title;
        
        if (navigator.share) {
            navigator.share({
                title: title,
                url: url
            });
        } else {
            // Fallback to copying URL
            navigator.clipboard.writeText(url).then(function() {
                showAlert('Link copied to clipboard!', 'success');
            });
        }
    });

    // Auto-refresh for real-time data
    if ($('.auto-refresh').length) {
        setInterval(function() {
            $('.auto-refresh').each(function() {
                var element = $(this);
                var url = element.data('refresh-url');
                
                $.get(url, function(data) {
                    element.html(data);
                });
            });
        }, 30000); // Refresh every 30 seconds
    }

    // Chart initialization (if Chart.js is loaded)
    if (typeof Chart !== 'undefined') {
        initializeCharts();
    }

    // Initialize date pickers
    if ($.fn.datepicker) {
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayHighlight: true
        });
    }

    // Initialize rich text editors
    if (typeof tinymce !== 'undefined') {
        tinymce.init({
            selector: '.rich-editor',
            height: 300,
            plugins: 'advlist autolink lists link image charmap print preview anchor',
            toolbar: 'undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help'
        });
    }
});

// Utility functions
function showAlert(message, type) {
    var alertClass = 'alert-' + type;
    var iconClass = type === 'success' ? 'fa-check-circle' : 
                   type === 'danger' ? 'fa-exclamation-circle' : 
                   type === 'warning' ? 'fa-exclamation-triangle' : 'fa-info-circle';
    
    var alert = $('<div class="alert ' + alertClass + ' alert-dismissible fade show" role="alert">' +
                  '<i class="fas ' + iconClass + ' me-2"></i>' + message +
                  '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>' +
                  '</div>');
    
    $('.main-content').prepend(alert);
    
    setTimeout(function() {
        alert.fadeOut('slow', function() {
            $(this).remove();
        });
    }, 5000);
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR'
    }).format(amount);
}

function formatDate(date) {
    return new Date(date).toLocaleDateString('en-IN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

function formatDateTime(date) {
    return new Date(date).toLocaleString('en-IN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function initializeCharts() {
    // Donation chart
    var donationCtx = document.getElementById('donationChart');
    if (donationCtx) {
        new Chart(donationCtx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                datasets: [{
                    label: 'Donations',
                    data: [12, 19, 3, 5, 2, 3],
                    borderColor: '#667eea',
                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    // Campaign progress chart
    var campaignCtx = document.getElementById('campaignChart');
    if (campaignCtx) {
        new Chart(campaignCtx, {
            type: 'doughnut',
            data: {
                labels: ['Completed', 'In Progress', 'Pending'],
                datasets: [{
                    data: [30, 50, 20],
                    backgroundColor: ['#28a745', '#667eea', '#ffc107']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false
            }
        });
    }
}

// Export functions for global use
window.NGOConnect = {
    showAlert: showAlert,
    formatCurrency: formatCurrency,
    formatDate: formatDate,
    formatDateTime: formatDateTime
};

