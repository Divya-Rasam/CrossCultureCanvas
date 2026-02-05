document.addEventListener('DOMContentLoaded', function() {
    // Get all filter buttons
    const filterButtons = document.querySelectorAll('.btn-outline-light');
    
    // Add click event to each button
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Remove active class from all buttons
            filterButtons.forEach(btn => btn.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Get the filter value
            const filter = this.textContent.trim();
            
            // Filter artists (this would be implemented with actual data)
            filterArtists(filter);
        });
    });
});

function filterArtists(category) {
    // This function would filter the artists based on category
    // For now, it's just a placeholder
    console.log('Filtering artists by category:', category);
    
    // In a real implementation, you would:
    // 1. Make an AJAX request to get filtered artists
    // 2. Update the DOM with the filtered results
}