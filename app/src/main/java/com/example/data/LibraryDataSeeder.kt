package com.example.data

import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.model.Book
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.Student

object LibraryDataSeeder {

    fun getInitialBooks(): List<Book> = listOf(
        Book(
            id = "b1",
            title = "Introduction to Algorithms",
            author = "Thomas H. Cormen, Charles E. Leiserson",
            isbn = "978-0262033848",
            genre = "Computer Science",
            publisher = "MIT Press",
            publicationYear = 2022,
            totalCopies = 8,
            availableCopies = 5,
            shelfLocation = "Floor 2 • Aisle 4 • Shelf B",
            description = "The leading textbook on computer algorithm analysis and design, covering dynamic programming, graph algorithms, and data structures.",
            coverGradientIndex = 0
        ),
        Book(
            id = "b2",
            title = "Designing Data-Intensive Applications",
            author = "Martin Kleppmann",
            isbn = "978-1449373320",
            genre = "System Design",
            publisher = "O'Reilly Media",
            publicationYear = 2021,
            totalCopies = 6,
            availableCopies = 1,
            shelfLocation = "Floor 2 • Aisle 4 • Shelf C",
            description = "The definitive guide to data systems, fault tolerance, replication, partitioning, and distributed transactions.",
            coverGradientIndex = 1
        ),
        Book(
            id = "b3",
            title = "Clean Architecture",
            author = "Robert C. Martin",
            isbn = "978-0134494166",
            genre = "Software Engineering",
            publisher = "Prentice Hall",
            publicationYear = 2018,
            totalCopies = 5,
            availableCopies = 2,
            shelfLocation = "Floor 2 • Aisle 5 • Shelf A",
            description = "Universal rules of software structure and architecture for building durable, maintainable systems.",
            coverGradientIndex = 2
        ),
        Book(
            id = "b4",
            title = "Deep Learning",
            author = "Ian Goodfellow, Yoshua Bengio",
            isbn = "978-0262035613",
            genre = "Artificial Intelligence",
            publisher = "MIT Press",
            publicationYear = 2020,
            totalCopies = 4,
            availableCopies = 0,
            shelfLocation = "Floor 3 • Aisle 1 • Shelf D",
            description = "Mathematical foundations and practical algorithms for neural networks, backpropagation, and representation learning.",
            coverGradientIndex = 3
        ),
        Book(
            id = "b5",
            title = "The Pragmatic Programmer",
            author = "David Thomas, Andrew Hunt",
            isbn = "978-0135957059",
            genre = "Software Engineering",
            publisher = "Addison-Wesley",
            publicationYear = 2019,
            totalCopies = 7,
            availableCopies = 4,
            shelfLocation = "Floor 2 • Aisle 5 • Shelf B",
            description = "From coding craft and testing to career philosophy, the timeless 20th anniversary edition guide.",
            coverGradientIndex = 4
        ),
        Book(
            id = "b6",
            title = "Artificial Intelligence: A Modern Approach",
            author = "Stuart Russell, Peter Norvig",
            isbn = "978-0134610993",
            genre = "Artificial Intelligence",
            publisher = "Pearson",
            publicationYear = 2023,
            totalCopies = 6,
            availableCopies = 3,
            shelfLocation = "Floor 3 • Aisle 1 • Shelf E",
            description = "The gold-standard reference for autonomous agents, heuristic search, logic, and probabilistic reasoning.",
            coverGradientIndex = 0
        ),
        Book(
            id = "b7",
            title = "Sapiens: A Brief History of Humankind",
            author = "Yuval Noah Harari",
            isbn = "978-0062316097",
            genre = "History",
            publisher = "Harper",
            publicationYear = 2018,
            totalCopies = 5,
            availableCopies = 3,
            shelfLocation = "Floor 1 • Aisle 2 • Shelf C",
            description = "A bold exploration of how biology and history have defined humans and created civilizations.",
            coverGradientIndex = 1
        ),
        Book(
            id = "b8",
            title = "Grokking Algorithms",
            author = "Aditya Y. Bhargava",
            isbn = "978-1617292231",
            genre = "Computer Science",
            publisher = "Manning",
            publicationYear = 2022,
            totalCopies = 5,
            availableCopies = 3,
            shelfLocation = "Floor 2 • Aisle 4 • Shelf A",
            description = "A fully illustrated, friendly guide that teaches you how to apply common algorithms to practical problems.",
            coverGradientIndex = 2
        ),
        Book(
            id = "b9",
            title = "Structure and Interpretation of Computer Programs",
            author = "Harold Abelson, Gerald Jay Sussman",
            isbn = "978-0262510875",
            genre = "Computer Science",
            publisher = "MIT Press",
            publicationYear = 2019,
            totalCopies = 4,
            availableCopies = 2,
            shelfLocation = "Floor 2 • Aisle 3 • Shelf D",
            description = "World-renowned computer science classic focusing on abstraction, recursion, and meta-linguistic processing.",
            coverGradientIndex = 3
        ),
        Book(
            id = "b10",
            title = "1984",
            author = "George Orwell",
            isbn = "978-0451524935",
            genre = "Literature",
            publisher = "Signet Classics",
            publicationYear = 2017,
            totalCopies = 10,
            availableCopies = 8,
            shelfLocation = "Floor 1 • Aisle 1 • Shelf B",
            description = "The dystopian masterpiece examining totalitarianism, surveillance, and truth in a controlled society.",
            coverGradientIndex = 4
        )
    )

    fun getInitialStudents(): List<Student> = listOf(
        Student(
            id = "s1",
            studentCode = "STU-2026-104",
            fullName = "Alexander Wright",
            email = "a.wright@university.edu",
            phone = "+1 (555) 234-5678",
            department = "Computer Science",
            yearLevel = "Senior (Year 4)",
            activeLoansCount = 1,
            totalLoansHistory = 14,
            unpaidFineAmount = 0.0
        ),
        Student(
            id = "s2",
            studentCode = "STU-2026-088",
            fullName = "Sophia Chen",
            email = "s.chen@university.edu",
            phone = "+1 (555) 345-6789",
            department = "Software Engineering",
            yearLevel = "Junior (Year 3)",
            activeLoansCount = 1,
            totalLoansHistory = 9,
            unpaidFineAmount = 0.0
        ),
        Student(
            id = "s3",
            studentCode = "STU-2026-215",
            fullName = "Marcus Johnson",
            email = "m.johnson@university.edu",
            phone = "+1 (555) 456-7890",
            department = "Data Science",
            yearLevel = "Sophomore (Year 2)",
            activeLoansCount = 2,
            totalLoansHistory = 11,
            unpaidFineAmount = 6.00
        ),
        Student(
            id = "s4",
            studentCode = "STU-2026-142",
            fullName = "Emily Rodriguez",
            email = "e.rodriguez@university.edu",
            phone = "+1 (555) 567-8901",
            department = "Information Systems",
            yearLevel = "Senior (Year 4)",
            activeLoansCount = 1,
            totalLoansHistory = 16,
            unpaidFineAmount = 0.0
        ),
        Student(
            id = "s5",
            studentCode = "STU-2026-309",
            fullName = "David Kim",
            email = "d.kim@university.edu",
            phone = "+1 (555) 678-9012",
            department = "Mathematics",
            yearLevel = "Freshman (Year 1)",
            activeLoansCount = 0,
            totalLoansHistory = 4,
            unpaidFineAmount = 0.0
        ),
        Student(
            id = "s6",
            studentCode = "STU-2026-177",
            fullName = "Aisha Patel",
            email = "a.patel@university.edu",
            phone = "+1 (555) 789-0123",
            department = "Artificial Intelligence",
            yearLevel = "Graduate (MSc)",
            activeLoansCount = 1,
            totalLoansHistory = 22,
            unpaidFineAmount = 0.0
        ),
        Student(
            id = "s7",
            studentCode = "STU-2026-064",
            fullName = "Lucas Miller",
            email = "l.miller@university.edu",
            phone = "+1 (555) 890-1234",
            department = "Electrical Engineering",
            yearLevel = "Junior (Year 3)",
            activeLoansCount = 1,
            totalLoansHistory = 7,
            unpaidFineAmount = 3.50
        ),
        Student(
            id = "s8",
            studentCode = "STU-2026-291",
            fullName = "Olivia Taylor",
            email = "o.taylor@university.edu",
            phone = "+1 (555) 901-2345",
            department = "Cyber Security",
            yearLevel = "Senior (Year 4)",
            activeLoansCount = 1,
            totalLoansHistory = 13,
            unpaidFineAmount = 2.00
        )
    )

    fun getInitialLoans(): List<Loan> = listOf(
        // Overdue Loans (fine calculated at $0.50 per day)
        Loan(
            id = "loan-01",
            bookId = "b2",
            bookTitle = "Designing Data-Intensive Applications",
            bookIsbn = "978-1449373320",
            studentId = "s3",
            studentName = "Marcus Johnson",
            studentCode = "STU-2026-215",
            borrowDate = "2026-09-24",
            dueDate = "2026-10-08",
            status = LoanStatus.OVERDUE,
            daysOverdue = 12,
            fineAmount = 6.00,
            notes = "First reminder sent via email"
        ),
        Loan(
            id = "loan-02",
            bookId = "b3",
            bookTitle = "Clean Architecture",
            bookIsbn = "978-0134494166",
            studentId = "s7",
            studentName = "Lucas Miller",
            studentCode = "STU-2026-064",
            borrowDate = "2026-09-29",
            dueDate = "2026-10-13",
            status = LoanStatus.OVERDUE,
            daysOverdue = 7,
            fineAmount = 3.50,
            notes = "Academic hold pending notice"
        ),
        Loan(
            id = "loan-03",
            bookId = "b4",
            bookTitle = "Deep Learning",
            bookIsbn = "978-0262035613",
            studentId = "s8",
            studentName = "Olivia Taylor",
            studentCode = "STU-2026-291",
            borrowDate = "2026-10-02",
            dueDate = "2026-10-16",
            status = LoanStatus.OVERDUE,
            daysOverdue = 4,
            fineAmount = 2.00,
            notes = "Automated SMS notification dispatched"
        ),
        // Active on-time Loans
        Loan(
            id = "loan-04",
            bookId = "b1",
            bookTitle = "Introduction to Algorithms",
            bookIsbn = "978-0262033848",
            studentId = "s1",
            studentName = "Alexander Wright",
            studentCode = "STU-2026-104",
            borrowDate = "2026-10-12",
            dueDate = "2026-10-26",
            status = LoanStatus.ACTIVE
        ),
        Loan(
            id = "loan-05",
            bookId = "b5",
            bookTitle = "The Pragmatic Programmer",
            bookIsbn = "978-0135957059",
            studentId = "s2",
            studentName = "Sophia Chen",
            studentCode = "STU-2026-088",
            borrowDate = "2026-10-14",
            dueDate = "2026-10-28",
            status = LoanStatus.ACTIVE
        ),
        Loan(
            id = "loan-06",
            bookId = "b6",
            bookTitle = "Artificial Intelligence: A Modern Approach",
            bookIsbn = "978-0134610993",
            studentId = "s4",
            studentName = "Emily Rodriguez",
            studentCode = "STU-2026-142",
            borrowDate = "2026-10-15",
            dueDate = "2026-10-29",
            status = LoanStatus.ACTIVE
        ),
        Loan(
            id = "loan-07",
            bookId = "b7",
            bookTitle = "Sapiens: A Brief History of Humankind",
            bookIsbn = "978-0062316097",
            studentId = "s6",
            studentName = "Aisha Patel",
            studentCode = "STU-2026-177",
            borrowDate = "2026-10-18",
            dueDate = "2026-11-01",
            status = LoanStatus.ACTIVE
        ),
        // Recently Returned Loans for Book History
        Loan(
            id = "loan-08",
            bookId = "b1",
            bookTitle = "Introduction to Algorithms",
            bookIsbn = "978-0262033848",
            studentId = "s5",
            studentName = "David Kim",
            studentCode = "STU-2026-309",
            borrowDate = "2026-09-15",
            dueDate = "2026-09-29",
            returnDate = "2026-09-28",
            status = LoanStatus.RETURNED
        ),
        Loan(
            id = "loan-09",
            bookId = "b2",
            bookTitle = "Designing Data-Intensive Applications",
            bookIsbn = "978-1449373320",
            studentId = "s1",
            studentName = "Alexander Wright",
            studentCode = "STU-2026-104",
            borrowDate = "2026-09-01",
            dueDate = "2026-09-15",
            returnDate = "2026-09-14",
            status = LoanStatus.RETURNED
        ),
        Loan(
            id = "loan-10",
            bookId = "b3",
            bookTitle = "Clean Architecture",
            bookIsbn = "978-0134494166",
            studentId = "s6",
            studentName = "Aisha Patel",
            studentCode = "STU-2026-177",
            borrowDate = "2026-09-10",
            dueDate = "2026-09-24",
            returnDate = "2026-09-23",
            status = LoanStatus.RETURNED
        )
    )

    fun getInitialActivities(): List<ActivityLog> = listOf(
        ActivityLog(
            id = "act-1",
            type = ActivityType.BORROW,
            title = "Book Issued: Sapiens",
            description = "Issued to Aisha Patel (STU-2026-177) • Due Nov 01, 2026",
            timestamp = "10 mins ago"
        ),
        ActivityLog(
            id = "act-2",
            type = ActivityType.RETURN,
            title = "Book Returned: Algorithms",
            description = "Returned in pristine condition by David Kim (STU-2026-309)",
            timestamp = "42 mins ago"
        ),
        ActivityLog(
            id = "act-3",
            type = ActivityType.OVERDUE_ALERT,
            title = "Overdue Notice Generated",
            description = "Marcus Johnson has 1 item overdue by 12 days (Fine: $6.00)",
            timestamp = "2 hours ago"
        ),
        ActivityLog(
            id = "act-4",
            type = ActivityType.BOOK_ADDED,
            title = "New Catalog Item Added",
            description = "'Artificial Intelligence: A Modern Approach' (6 copies stocked)",
            timestamp = "Yesterday"
        ),
        ActivityLog(
            id = "act-5",
            type = ActivityType.FINE_COLLECTED,
            title = "Fine Settlement Recorded",
            description = "Collected $4.50 late fee from Emily Rodriguez via Admin terminal",
            timestamp = "Yesterday"
        )
    )
}
