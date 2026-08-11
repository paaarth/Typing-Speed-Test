package com.speedtype.seed;

import com.speedtype.model.Difficulty;
import com.speedtype.model.Paragraph;
import com.speedtype.model.Topic;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ParagraphRepository paragraphRepository;
    private final TopicRepository topicRepository;

    public DataSeeder(ParagraphRepository paragraphRepository, TopicRepository topicRepository) {
        this.paragraphRepository = paragraphRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public void run(String... args) {
        if (paragraphRepository.count() > 0) {
            return;
        }

        Map<String, Topic> topics = seedTopics();
        List<Paragraph> paragraphs = new ArrayList<>();

        seedTechnology(paragraphs, topics.get("TECHNOLOGY"));
        seedNature(paragraphs, topics.get("NATURE"));
        seedSports(paragraphs, topics.get("SPORTS"));
        seedScience(paragraphs, topics.get("SCIENCE"));
        seedMotivation(paragraphs, topics.get("MOTIVATION"));
        seedLiterature(paragraphs, topics.get("LITERATURE"));
        seedBusiness(paragraphs, topics.get("BUSINESS"));
        seedHistory(paragraphs, topics.get("HISTORY"));

        paragraphRepository.saveAll(paragraphs);
    }

    /** Creates the 8 starter topics if the topics table is empty, then returns a
     *  name -> Topic lookup either way (freshly created, or already existing from
     *  a previous run). */
    private Map<String, Topic> seedTopics() {
        if (topicRepository.count() == 0) {
            List<Topic> defaults = new ArrayList<>();
            defaults.add(newTopic("TECHNOLOGY", "cpu"));
            defaults.add(newTopic("NATURE", "leaf"));
            defaults.add(newTopic("SPORTS", "trophy"));
            defaults.add(newTopic("SCIENCE", "flask"));
            defaults.add(newTopic("MOTIVATION", "flame"));
            defaults.add(newTopic("LITERATURE", "book"));
            defaults.add(newTopic("BUSINESS", "briefcase"));
            defaults.add(newTopic("HISTORY", "landmark"));
            topicRepository.saveAll(defaults);
        }

        Map<String, Topic> byName = new HashMap<>();
        for (Topic t : topicRepository.findAll()) {
            byName.put(t.getName(), t);
        }
        return byName;
    }

    private Topic newTopic(String name, String icon) {
        Topic topic = new Topic();
        topic.setName(name);
        topic.setIcon(icon);
        return topic;
    }

    private void add(List<Paragraph> list, Topic topic, Difficulty difficulty, String text) {
        Paragraph paragraph = new Paragraph();
        paragraph.setTopic(topic);
        paragraph.setDifficulty(difficulty);
        paragraph.setText(text);
        paragraph.setWordCount(text.trim().split("\\s+").length);
        list.add(paragraph);
    }

    private void seedTechnology(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "Computers help us work, learn, and play games every day. A phone can call people far away in just a few seconds. Technology makes life easier for everyone.");
        add(list, topic, Difficulty.EASY,
            "Many people use apps to order food or book a ride. A simple tap on the screen can do so much. New gadgets come out every single year.");
        add(list, topic, Difficulty.MEDIUM,
            "Smartphones have changed how people communicate, shop, and stay informed about the world. With just a few taps, anyone can video call a friend across the globe or read breaking news the moment it happens.");
        add(list, topic, Difficulty.MEDIUM,
            "Cloud storage lets people save photos, documents, and videos without filling up their own devices. Instead of carrying a hard drive everywhere, users can access their files from any computer connected to the internet.");
        add(list, topic, Difficulty.HARD,
            "Artificial intelligence, once confined to research labs and science fiction novels, now quietly powers recommendation engines, voice assistants, and fraud detection systems, prompting engineers and policymakers alike to grapple with questions of transparency, bias, and accountability that did not exist a generation ago.");
        add(list, topic, Difficulty.HARD,
            "As quantum computing edges closer to practical viability, cryptographers are racing to develop algorithms resistant to attacks that could someday render today's encryption standards obsolete, a shift that promises to reshape everything from banking security to private communication.");
    }

    private void seedNature(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "The sun rises every morning and sets every night. Birds sing in the trees while the wind blows softly. Nature gives us fresh air and green grass to enjoy.");
        add(list, topic, Difficulty.EASY,
            "Rivers flow down from tall mountains into the wide blue sea. Flowers bloom in spring after the cold winter ends. Animals roam freely through the quiet forest.");
        add(list, topic, Difficulty.MEDIUM,
            "Forests are home to countless species of plants and animals, each playing a role in a delicate web of life. When rain falls on the canopy, it slowly filters down to nourish roots deep beneath the soil.");
        add(list, topic, Difficulty.MEDIUM,
            "Coral reefs, often called the rainforests of the sea, provide shelter for thousands of colorful fish and other marine creatures. Even small changes in ocean temperature can threaten these fragile underwater ecosystems.");
        add(list, topic, Difficulty.HARD,
            "Migratory birds travel thousands of miles each year, navigating by the stars, the earth's magnetic field, and even the scent of the wind, an astonishing feat of instinct that scientists are still working to fully understand despite decades of dedicated research.");
        add(list, topic, Difficulty.HARD,
            "Glaciers, though they appear motionless to the casual observer, are in fact slow rivers of ice, grinding against bedrock over centuries and carving valleys that will remain long after the ice itself has receded into memory.");
    }

    private void seedSports(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "Soccer is played with a round ball and two goals. Players run fast and kick the ball to score. Millions of fans cheer for their favorite teams.");
        add(list, topic, Difficulty.EASY,
            "Basketball players try to throw the ball through a high hoop. The game is fast and full of jumping and running. Teams need good passing to win.");
        add(list, topic, Difficulty.MEDIUM,
            "Marathon runners train for months before race day, building endurance through long runs and careful nutrition. Crossing the finish line after twenty-six miles feels like a reward for countless early mornings.");
        add(list, topic, Difficulty.MEDIUM,
            "A great tennis match often comes down to focus under pressure, as players trade powerful serves and quick volleys while the crowd holds its breath during every deciding point.");
        add(list, topic, Difficulty.HARD,
            "Championship teams are rarely built overnight; they emerge from years of disciplined recruiting, patient coaching, and a culture that rewards accountability over individual glory, lessons that often matter more than raw talent when the pressure of a decisive match finally arrives.");
        add(list, topic, Difficulty.HARD,
            "Endurance athletes speak of hitting a wall, a point at which glycogen stores are nearly depleted and every stride demands conscious willpower, yet it is precisely this threshold that separates casual competitors from those who have trained their minds as rigorously as their bodies.");
    }

    private void seedScience(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "Water is made of tiny parts called atoms. Plants use sunlight to make their own food. Scientists study the world to learn new things.");
        add(list, topic, Difficulty.EASY,
            "The moon orbits around the earth once a month. Gravity is a force that pulls objects toward the ground. Every living thing is made of small cells.");
        add(list, topic, Difficulty.MEDIUM,
            "Vaccines work by training the immune system to recognize a specific virus or bacteria before an actual infection occurs, allowing the body to respond quickly and effectively if it ever encounters the real threat.");
        add(list, topic, Difficulty.MEDIUM,
            "Volcanoes form when molten rock beneath the earth's crust finds a weak point and forces its way to the surface, sometimes building mountains over thousands of years and sometimes erupting with sudden, dramatic force.");
        add(list, topic, Difficulty.HARD,
            "Photosynthesis, the elegant biochemical process by which plants convert carbon dioxide and water into glucose using nothing more than sunlight, remains one of the most efficient energy-conversion systems ever discovered, inspiring decades of research into artificial versions that might one day power our own technology.");
        add(list, topic, Difficulty.HARD,
            "Neuroscientists have long debated how consciousness arises from the coordinated firing of billions of neurons, a question that sits at the uneasy intersection of biology, philosophy, and physics, and one that no single experiment has yet come close to resolving definitively.");
    }

    private void seedMotivation(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "Every big goal starts with one small step. Hard work always pays off in the end. Believe in yourself and never give up.");
        add(list, topic, Difficulty.EASY,
            "Mistakes help us learn and grow stronger. Today is a new chance to try again. Small steps taken daily lead to big change.");
        add(list, topic, Difficulty.MEDIUM,
            "Success rarely happens overnight; it is usually the quiet result of showing up consistently, even on the days when motivation is nowhere to be found and progress feels impossibly slow.");
        add(list, topic, Difficulty.MEDIUM,
            "The people who inspire us most are rarely the ones who never failed, but the ones who kept moving forward after every single setback, treating each stumble as one more lesson on the way to something better.");
        add(list, topic, Difficulty.HARD,
            "Discipline, far more reliably than fleeting bursts of motivation, is what carries people through the long, unglamorous middle of any worthwhile pursuit, the stretch where progress is invisible, encouragement is scarce, and only the quiet decision to continue anyway makes the difference.");
        add(list, topic, Difficulty.HARD,
            "Growth rarely announces itself with fanfare; more often it accumulates in ordinary, unremarkable moments, a single extra rep, a difficult conversation finally had, a habit repeated one more day than the day before, until one morning you notice you have become someone new.");
    }

    private void seedLiterature(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "A good story has a beginning, a middle, and an end. Books can take us to far away places. Reading every day helps build a bigger vocabulary.");
        add(list, topic, Difficulty.EASY,
            "Poets choose their words with great care. A short story can still teach a big lesson. Libraries are full of adventures waiting to be found.");
        add(list, topic, Difficulty.MEDIUM,
            "Great novels often endure not because of their plots alone, but because they capture something true about human nature that readers recognize in themselves, generation after generation, regardless of when or where the story was written.");
        add(list, topic, Difficulty.MEDIUM,
            "A skilled author can make an imaginary world feel more real than the room you are sitting in, weaving details so vivid that readers forget, if only for a chapter, that none of it actually happened.");
        add(list, topic, Difficulty.HARD,
            "The unreliable narrator, a device as old as storytelling itself, forces readers to question not just what is being said but who is saying it and why, turning the act of reading into an act of quiet detective work that rewards careful, skeptical attention.");
        add(list, topic, Difficulty.HARD,
            "Classic tragedies endure across centuries not because audiences enjoy watching characters suffer, but because they recognize, uncomfortably, a version of their own flaws magnified on stage, a mirror held up under the guise of entertainment.");
    }

    private void seedBusiness(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "A good idea can turn into a strong business. Customers like to feel valued and heard. Hard work and patience help a company grow.");
        add(list, topic, Difficulty.EASY,
            "Every company needs a clear plan to succeed. Saving money early helps during hard times. Good teamwork makes a business run smoothly.");
        add(list, topic, Difficulty.MEDIUM,
            "Startups often succeed not because they had the best original idea, but because they were willing to listen closely to customers and adjust their plans faster than larger, slower competitors could react.");
        add(list, topic, Difficulty.MEDIUM,
            "Building a strong brand takes more than a clever logo; it requires consistent quality and honest communication that customers come to trust over months and years of everyday interactions.");
        add(list, topic, Difficulty.HARD,
            "Negotiation, contrary to popular belief, is rarely won through aggression; the most successful negotiators typically succeed by understanding the other party's underlying interests so thoroughly that they can propose solutions neither side had initially considered, turning a potential conflict into genuine collaboration.");
        add(list, topic, Difficulty.HARD,
            "Companies that survive economic downturns are rarely the ones with the deepest pockets, but rather those with a culture disciplined enough to cut costs quickly, communicate honestly with employees, and resist the temptation to abandon long-term strategy for short-term relief.");
    }

    private void seedHistory(List<Paragraph> list, Topic topic) {
        add(list, topic, Difficulty.EASY,
            "Long ago, people traveled by horse or on foot. Old castles still stand in many countries today. History teaches us about how people once lived.");
        add(list, topic, Difficulty.EASY,
            "Ancient people built great structures without modern tools. Maps have changed a lot over hundreds of years. Learning about the past helps us understand today.");
        add(list, topic, Difficulty.MEDIUM,
            "Ancient trade routes connected distant civilizations long before modern transportation existed, allowing spices, silk, and ideas to travel thousands of miles between merchants who often never met each other in person.");
        add(list, topic, Difficulty.MEDIUM,
            "Many inventions we consider modern actually have surprisingly old roots, refined and rediscovered across centuries by different cultures who each solved the same basic problem in their own resourceful way.");
        add(list, topic, Difficulty.HARD,
            "Empires throughout history have often collapsed not from a single dramatic defeat but from a slow accumulation of overextension, internal corruption, and economic strain, a pattern so consistent across centuries and continents that historians still debate whether it holds any lesson for the present.");
        add(list, topic, Difficulty.HARD,
            "The printing press, though it seems unremarkable by today's standards, arguably reshaped human civilization more thoroughly than almost any invention before or since, democratizing access to knowledge and quietly setting the stage for reformations, revolutions, and scientific advances that followed in its wake.");
    }
}
