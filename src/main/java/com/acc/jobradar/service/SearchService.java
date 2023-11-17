package com.acc.jobradar.service;

import com.acc.jobradar.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    public List<String> getSuggestions(String userInput) {
        return List.of("Software","Software Engineer","Software Developer","Software Engineer Intern");
    }

    public List<JobPosting> searchJobPosting(String userInput) {
        return List.of(JobPosting.builder()
                .jobTitle("Software Development Co-op/Intern")
                .company("Nokia")
                .location("Ottawa, ON")
                .description("Position: Software Development Co-op/Intern\n" +
                        "\n" +
                        "Duration: 4-8 Months\n" +
                        "\n" +
                        "Date: May - Aug 2024 or May - Dec 2024\n" +
                        "\n" +
                        "Location: Virtual\n" +
                        "\n" +
                        "Come create the technology that helps the world act together\n" +
                        "\n" +
                        "We are a B2B technology innovation leader pioneering the future where networks meet cloud. At Nokia, you will have a positive impact on people’s lives and help build the capabilities needed for a more productive, sustainable, and accessible world.\n" +
                        "\n" +
                        "Be part of a culture built on an inclusive way of working where we are open to your ideas, you are empowered to take risks, and are encouraged to be fearless in bringing your authentic self to work.\n" +
                        "\n" +
                        "The team you'll be part of\n" +
                        "\n" +
                        "Network Infrastructure\n" +
                        "\n" +
                        "The pandemic has highlighted how important telecoms networks are to society. Nokia’s Network Infrastructure group is at the heart of a revolution to bring more and faster network capacity to people worldwide through our ambition, innovation, and technical expertise.\n" +
                        "\n" +
                        "Education Recommendations\n" +
                        "\n" +
                        "Currently a candidate for a bachelor’s degree or diploma in Computer Science, Computer Systems Engineering, etc., or a related field with an accredited school in Canada.\n" +
                        "\n" +
                        "What You Will Learn And Contribute To\n" +
                        "\n" +
                        "As part of our team, you will:\n" +
                        "\n" +
                        " Design and develop Software solutions. \n" +
                        " Conduct unit testing and troubleshooting. \n" +
                        " Evaluate and improve existing software solutions. \n" +
                        " Develop and execute database queries and conduct analysis. \n" +
                        "\n" +
                        "What is Nokia looking for from me\n" +
                        "\n" +
                        "you have:\n" +
                        "\n" +
                        " Initiative to continually learn, adapt, and grow. \n" +
                        " Have an inherent interest in being involved and helping others. \n" +
                        " Interested in determining the source of a problem and finding an effective solution. \n" +
                        " Ability to actively listen and communicate ideas with confidence. \n" +
                        "\n" +
                        "It would be nice if you also had:\n" +
                        "\n" +
                        " Excellent coding skills in C# .NET Visual Studio, Python, .NET, Java, React JS \n" +
                        " Relational Database and SQL Language, specifically Oracle, SQL Server, MySQL \n" +
                        " Knowledge of ETL, Data Warehouse / Solution Design \n" +
                        " Platforms: MS Windows Server, Unix/Linux \n" +
                        "\n" +
                        "What We Offer\n" +
                        "\n" +
                        "Nokia offers flexible and hybrid working schemes, continuous learning opportunities, well-being programs to support you mentally and physically, opportunities to join and get supported by employee resource groups, mentoring programs and highly diverse teams with an inclusive culture where people thrive and are empowered.\n" +
                        "\n" +
                        "Company Culture : We believe our people are our greatest asset and we aim to foster a fun, challenging, positive, and inclusive work environment. We offer 1-on-1 mentorship, learning and development opportunities, global impact and interaction, and flexible work/life balance.\n" +
                        "\n" +
                        "Meaningful Co-op Term : During your co-op term, you will have the opportunity to test out the latest technologies and work on innovative projects that could leave a global impact. You get to use skills learned in the classroom and apply it to real work situations while gaining professional hands-on experience.\n" +
                        "\n" +
                        "Nokia is committed to inclusion and is an equal-opportunity employer \n" +
                        "\n" +
                        "Nokia has received the following recognitions for its commitment to inclusion & equality:\n" +
                        "\n" +
                        " One of the World’s Most Ethical Companies by Ethisphere \n" +
                        " Gender-Equality Index by Bloomberg \n" +
                        " Workplace Pride Global Benchmark \n" +
                        "\n" +
                        "At Nokia, we act inclusively and respect the uniqueness of people.\n" +
                        "\n" +
                        "Nokia’s employment decisions are made regardless of race, color, national or ethnic origin, religion, gender, sexual orientation, gender identity or expression, age, marital status, disability, protected veteran status or other characteristics protected by law.\n" +
                        "\n" +
                        "We are committed to a culture of inclusion built upon our core value of respect.\n" +
                        "\n" +
                        "Join us and be part of a company where you will feel included and empowered to succeed.\n" +
                        "\n" +
                        "Follow and engage with us on social media:\n" +
                        "\n" +
                        "LinkedIn | Facebook | Instagram | Twitter @NokiaCareers\n" +
                        "\n" +
                        "Chat with our Nokia insiders to ask questions and learn more about us!\n" +
                        "\n" +
                        "Job\n" +
                        "\n" +
                        "Trainee - Trainee\n" +
                        "\n" +
                        "Primary Location\n" +
                        "\n" +
                        "North Americas-Canada-Ontario-Ottawa\n" +
                        "\n" +
                        "Schedule\n" +
                        "\n" +
                        "Full-time")
                .url("https://www.linkedin.com/jobs/search/?currentJobId=3737494173")
                .build());
    }
}
