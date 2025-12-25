document.addEventListener('DOMContentLoaded', function() {
    // Get all filter buttons
    const filterButtons = document.querySelectorAll('.filter-btn');
    
    // Get all media items
    const mediaItems = document.querySelectorAll('.media-item');
    
    // Add click event to each button
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Remove active class from all buttons
            filterButtons.forEach(btn => btn.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Get the filter value
            const filter = this.getAttribute('data-filter');
            
            // Filter media items
            filterMediaItems(filter);
        });
    });
    
    function filterMediaItems(filter) {
        mediaItems.forEach(item => {
            if (filter === 'all') {
                item.style.display = 'block';
            } else {
                const itemType = item.getAttribute('data-type');
                if (itemType === filter) {
                    item.style.display = 'block';
                } else {
                    item.style.display = 'none';
                }
            }
        });
    }
});