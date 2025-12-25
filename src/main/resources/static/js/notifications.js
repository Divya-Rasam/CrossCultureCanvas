document.addEventListener('DOMContentLoaded', function() {
    // Mark as read functionality
    const markAsReadButtons = document.querySelectorAll('.mark-as-read');
    markAsReadButtons.forEach(button => {
        button.addEventListener('click', function() {
            const notificationId = this.getAttribute('data-id');
            
            fetch(`/notifications/mark-as-read/${notificationId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                }
            })
            .then(response => response.text())
            .then(data => {
                if (data === 'success') {
                    // Remove the info background
                    const notificationItem = this.closest('.list-group-item');
                    notificationItem.classList.remove('list-group-item-info');
                    
                    // Hide the button
                    this.style.display = 'none';
                    
                    // Update notification count in navigation
                    updateNotificationCount();
                }
            })
            .catch(error => {
                console.error('Error marking notification as read:', error);
            });
        });
    });
    
    // Mark all as read functionality
    const markAllAsReadButton = document.getElementById('markAllAsRead');
    if (markAllAsReadButton) {
        markAllAsReadButton.addEventListener('click', function() {
            fetch('/notifications/mark-all-as-read', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                }
            })
            .then(response => response.text())
            .then(data => {
                if (data === 'success') {
                    // Remove all info backgrounds
                    const notificationItems = document.querySelectorAll('.list-group-item-info');
                    notificationItems.forEach(item => {
                        item.classList.remove('list-group-item-info');
                    });
                    
                    // Hide all mark as read buttons
                    const markAsReadButtons = document.querySelectorAll('.mark-as-read');
                    markAsReadButtons.forEach(button => {
                        button.style.display = 'none';
                    });
                    
                    // Update notification count in navigation
                    updateNotificationCount();
                }
            })
            .catch(error => {
                console.error('Error marking all notifications as read:', error);
            });
        });
    }
    
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
            })
            .catch(error => {
                console.error('Error updating notification count:', error);
            });
    }
});