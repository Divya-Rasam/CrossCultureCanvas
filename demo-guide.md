Cross Culture Canvas - Demo Guide
Introduction
Cross Culture Canvas is a platform that connects underground artists with venues and audiences. This demo will walk you through the key features of the platform.

Demo Flow
1. Landing Page
Start at the homepage: http://localhost:8080
Notice the video background showcasing urban culture
View the featured artists section with sample artists
2. User Registration
Click on "Register" in the navigation
Fill out the registration form:
Username: demo_artist
Email: demo@example.com
Password: password
Role: ARTIST
Click "Register"
You'll be redirected to the login page
3. User Login
Enter your credentials:
Username: demo_artist
Password: password
Click "Login"
You'll be redirected to your dashboard
4. Creating an Artist Profile
If you don't have an artist profile, you'll see a button to create one
Click "Create Artist Profile"
Fill out the form:
Artist Name: Demo Artist
Category: RAPPER
Bio: Underground rapper with a unique flow
Social media links (optional)
Click "Save Profile"
5. Uploading Media
From the dashboard, click "My Media" in the navigation
Click "Upload Media"
Fill out the form:
Title: My First Track
Description: A sample track for demo
Media Type: AUDIO
URL: https://soundcloud.com/example
Click "Save Media"
6. Browsing Venues
Click "Venues" in the navigation
Browse the list of available venues
Click on "The Underground Spot" to view details
7. Booking a Venue
From the venue details page, click "Book Venue"
Fill out the booking form:
Event Title: Demo Performance
Event Description: A demo performance for the platform
Date: Select a future date
Time: Select a time slot
Click "Submit Booking Request"
8. Venue Owner Actions
Logout and login as a venue owner:
Username: venue_owner
Password: password
Go to "My Venues" in the navigation
Click on "Venue Bookings"
Approve the demo booking request
9. Viewing Notifications
As the artist, you should receive a notification that your booking was approved
Click on the notification badge in the navigation
View the notification details
10. Searching for Content
Use the search bar in the navigation
Try searching for "rapper" or "venue"
Browse the search results
Sample Login Credentials
Artist Account
Username: underground_rapper
Password: password
Graffiti Artist Account
Username: graffiti_king
Password: password
Skateboarder Account
Username: skate_master
Password: password
Venue Owner Account
Username: venue_owner
Password: password
Database Schema
The application uses the following main tables:

users: User accounts and authentication
artists: Artist profiles and information
venues: Venue details and availability
events: Event information and scheduling
media: Media files uploaded by artists
bookings: Booking requests and status
notifications: User notifications
Key Features Demonstrated
User Authentication: Registration and login with role-based access
Artist Profiles: Creating and managing artist profiles
Media Management: Uploading and showcasing artist work
Venue Booking: Requesting and managing venue bookings
Notifications: Real-time updates on booking status
Search Functionality: Finding artists, venues, and events
Dashboard: Centralized hub for user activity
Conclusion
Cross Culture Canvas provides a comprehensive platform for underground artists to showcase their work, connect with venues, and reach audiences. The demo illustrates the complete user journey from registration to booking a venue.
























Step 5: Create a Presentation
Create a PowerPoint presentation that showcases your application. Include:

Introduction
Project overview
Problem statement
Solution approach
System Architecture
Technology stack (Spring Boot, Thymeleaf, MySQL, etc.)
Database schema diagram
System components
Key Features
User authentication and roles
Artist profiles and media management
Venue booking system
Notification system
Search functionality
User dashboard
Demo Screenshots
Landing page
Registration and login
Artist profile creation
Media upload
Venue browsing and booking
Notification system
Dashboard
Database Design
Entity-relationship diagram
Sample data
Key queries
Challenges and Solutions
Technical challenges faced
How they were overcome
Lessons learned
Future Enhancements
Payment integration
Mobile app
Advanced analytics
Social features
Step 6: Practice the Demo
Practice walking through the demo flow multiple times:

Start the application
Walk through each feature
Explain what's happening at each step
Be prepared to answer questions about the implementation
Have backup plans in case something goes wrong during the demo
Step 7: Prepare for Questions
Anticipate questions you might be asked:

Why did you choose Spring Boot for this project?
How does the notification system work?
How did you handle the many-to-many relationships in the database?
What security measures have you implemented?
How would you scale this application for more users?
What challenges did you face during development?
Step 8: Create a One-Page Summary
Create a one-page summary document that highlights the key aspects of your project:

Cross Culture Canvas - Project Summary
Project Overview
Cross Culture Canvas is a web platform designed to connect underground artists with venues and audiences. It provides a space for artists to showcase their work, find performance spaces, and connect with their audience.

Key Features
User authentication with role-based access
Artist profiles with media management
Venue booking system
Real-time notifications
Search functionality
User dashboard
Technology Stack
Backend: Spring Boot, Java, JPA/Hibernate
Frontend: Thymeleaf, HTML, CSS, JavaScript
Database: H2 (for demo) / MySQL (for production)
Security: Spring Security
Database Schema
The application uses a relational database with tables for users, artists, venues, events, media, bookings, and notifications.

Demo Credentials
Artist: underground_rapper / password
Graffiti Artist: graffiti_king / password
Skateboarder: skate_master / password
Venue Owner: venue_owner / password
Project Highlights
Complete user journey from registration to booking
Real-time notifications for booking updates
Media management for artists
Search functionality across all content types
Role-based access control