document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Auto-refresh notifications count
    setInterval(updateNotificationCount, 30000); // Every 30 seconds
});

function updateNotificationCount() {
    fetch('/notifications/unread-count')
        .then(response => response.json())
        .then(data => {
            // Update notification badge in navigation
            const badge = document.querySelector('.navbar-nav .badge.bg-danger');
            if (badge) {
                if (data.count > 0) {
                    badge.textContent = data.count;
                    badge.style.display = 'inline-block';
                } else {
                    badge.style.display = 'none';
                }
            }
            
            // Update notification count in dashboard card
            const dashboardBadge = document.querySelector('.bg-info .fa-bell').parentElement.querySelector('h4');
            if (dashboardBadge) {
                dashboardBadge.textContent = data.count;
            }
        })
        .catch(error => {
            console.error('Error updating notification count:', error);
        });
}