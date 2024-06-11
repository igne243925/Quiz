package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizScreen extends AppCompatActivity {
    private TextView questionText;
    Button backbutton, submitbutton;
    private Button choice1, choice2, choice3, choice4;

    private List<String> choices;
    private String correctAnswer;
    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int selectedChoiceIndex = -1;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        questionText = findViewById(R.id.questionText);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);
        backbutton = findViewById(R.id.backBtn);
        submitbutton = findViewById(R.id.submitBtn);

        String subject = getIntent().getStringExtra("SUBJECT");

        setupQuestions(subject);
        displayCurrentQuestion();


  // choices
        choice1.setOnClickListener(view -> selectChoice(0));
        choice2.setOnClickListener(view -> selectChoice(1));
        choice3.setOnClickListener(view -> selectChoice(2));
        choice4.setOnClickListener(view -> selectChoice(3));

        backbutton.setOnClickListener(view -> {
            // Start QuickGameScreen activity and finish current one
            startActivity(new Intent(QuizScreen.this, QuickGameScreen.class));
            finish();
        });

        submitbutton.setOnClickListener(view -> submitAnswer());
    }



    //questions
    private void setupQuestions(String subject) {
        questionList = new ArrayList<>();

        //  questions for each subject
        switch (subject) {
            case "Math":
                questionList.add(new QuizQuestion("What is 5 * 5?", "25", Arrays.asList("20", "25", "30", "35")));
                questionList.add(new QuizQuestion("What is the square root of 64?", "8", Arrays.asList("6", "7", "8", "9")));
                questionList.add(new QuizQuestion("What is 7 + 8?", "15", Arrays.asList("14", "15", "16", "17")));
                questionList.add(new QuizQuestion("What is 10 divided by 2?", "5", Arrays.asList("4", "5", "6", "7")));
                questionList.add(new QuizQuestion("What is 9 - 3?", "6", Arrays.asList("5", "6", "7", "8")));
                questionList.add(new QuizQuestion("What is 3 * 7?", "21", Arrays.asList("18", "19", "20", "21")));
                questionList.add(new QuizQuestion("What is 6 + 9?", "15", Arrays.asList("12", "13", "14", "15")));
                questionList.add(new QuizQuestion("What is the cube root of 27?", "3", Arrays.asList("2", "3", "4", "5")));
                questionList.add(new QuizQuestion("What is 15 / 3?", "5", Arrays.asList("4", "5", "6", "7")));
                questionList.add(new QuizQuestion("What is 12 + 15?", "27", Arrays.asList("25", "26", "27", "28")));
                break;
            case "English":
                questionList.add(new QuizQuestion("Who wrote 'Pride and Prejudice'?", "Jane Austen",
                        Arrays.asList("Charlotte Brontë", "Emily Brontë", "Jane Austen", "George Eliot")));

                questionList.add(new QuizQuestion("What is the synonym for 'happy'?", "Joyful",
                        Arrays.asList("Sad", "Joyful", "Angry", "Bored")));

                questionList.add(new QuizQuestion("Which of the following is a verb?", "Run",
                        Arrays.asList("Happy", "Run", "Beautiful", "Quick")));

                questionList.add(new QuizQuestion("What is the antonym of 'brave'?", "Cowardly",
                        Arrays.asList("Courageous", "Bold", "Fearless", "Cowardly")));

                questionList.add(new QuizQuestion("Who is the author of 'The Great Gatsby'?", "F. Scott Fitzgerald",
                        Arrays.asList("Ernest Hemingway", "F. Scott Fitzgerald", "Mark Twain", "John Steinbeck")));

                questionList.add(new QuizQuestion("What is the superlative form of 'good'?", "Best",
                        Arrays.asList("Better", "Good", "Best", "Excellent")));

                questionList.add(new QuizQuestion("Which is a literary device used to represent an abstract idea?", "Symbolism",
                        Arrays.asList("Irony", "Symbolism", "Metaphor", "Simile")));

                questionList.add(new QuizQuestion("What is the plural form of 'child'?", "Children",
                        Arrays.asList("Childs", "Childrens", "Children", "Childes")));

                questionList.add(new QuizQuestion("What is the comparative form of 'fast'?", "Faster",
                        Arrays.asList("Fastest", "Fast", "Faster", "More Fast")));

                questionList.add(new QuizQuestion("Who wrote 'Moby-Dick'?", "Herman Melville",
                        Arrays.asList("Nathaniel Hawthorne", "Herman Melville", "Edgar Allan Poe", "Charles Dickens")));
                break;
            case "Science":
                questionList.add(new QuizQuestion("Which planet is known as the Red Planet?", "Mars", Arrays.asList("Earth", "Mars", "Jupiter", "Venus")));

                questionList.add(new QuizQuestion("What is the chemical symbol for gold?", "Au", Arrays.asList("Ag", "Au", "Pt", "Pb")));

                questionList.add(new QuizQuestion("What is the largest organ in the human body?", "Skin", Arrays.asList("Liver", "Skin", "Heart", "Lungs")));

                questionList.add(new QuizQuestion("Which gas do plants absorb from the atmosphere?", "Carbon Dioxide", Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Methane")));

                questionList.add(new QuizQuestion("Which element is common to all organic compounds?", "Carbon", Arrays.asList("Oxygen", "Carbon", "Hydrogen", "Nitrogen")));

                questionList.add(new QuizQuestion("What is the powerhouse of the cell?", "Mitochondria", Arrays.asList("Nucleus", "Mitochondria", "Ribosome", "Golgi apparatus")));

                questionList.add(new QuizQuestion("Which planet is known for its rings?", "Saturn", Arrays.asList("Jupiter", "Saturn", "Uranus", "Neptune")));

                questionList.add(new QuizQuestion("What force keeps us on the ground?", "Gravity", Arrays.asList("Magnetism", "Friction", "Gravity", "Inertia")));

                questionList.add(new QuizQuestion("Which part of the atom has a positive charge?", "Proton", Arrays.asList("Proton", "Neutron", "Electron", "Nucleus")));

                questionList.add(new QuizQuestion("Which famous scientist developed the theory of relativity?", "Einstein", Arrays.asList("Newton", "Einstein", "Tesla", "Galileo")));
                break;
            case "History":
                questionList.add(new QuizQuestion(
                        "Who was the first President of the Philippines?",
                        "Emilio Aguinaldo",
                        Arrays.asList("Emilio Aguinaldo", "Manuel Quezon", "Ferdinand Marcos", "Jose Rizal")
                ));

                questionList.add(new QuizQuestion(
                        "When did the Philippines gain independence from Spain?",
                        "June 12, 1898",
                        Arrays.asList("June 12, 1898", "July 4, 1946", "August 21, 1983", "February 25, 1986")
                ));

                questionList.add(new QuizQuestion(
                        "Who is considered the national hero of the Philippines?",
                        "Jose Rizal",
                        Arrays.asList("Jose Rizal", "Andres Bonifacio", "Emilio Aguinaldo", "Apolinario Mabini")
                ));

                questionList.add(new QuizQuestion(
                        "What was the name of the revolutionary group led by Andres Bonifacio?",
                        "Katipunan",
                        Arrays.asList("Katipunan", "Hukbalahap", "Magdalo", "Ilustrados")
                ));

                questionList.add(new QuizQuestion(
                        "Who was the first Filipino to travel in outer space?",
                        "No Filipino has traveled to outer space",
                        Arrays.asList("Edgar D. Mitchell", "No Filipino has traveled to outer space", "Carlos P. Romulo", "Fidel V. Ramos")
                ));

                questionList.add(new QuizQuestion(
                        "In which year did the EDSA People Power Revolution occur?",
                        "1986",
                        Arrays.asList("1986", "1972", "1991", "1998")
                ));

                questionList.add(new QuizQuestion(
                        "Who was the President of the Philippines during World War II?",
                        "Manuel L. Quezon",
                        Arrays.asList("Manuel L. Quezon", "Jose P. Laurel", "Elpidio Quirino", "Sergio Osmeña")
                ));

                questionList.add(new QuizQuestion(
                        "Which event triggered the declaration of Martial Law in the Philippines in 1972?",
                        "The Plaza Miranda bombing",
                        Arrays.asList("The Plaza Miranda bombing", "The Jabidah massacre", "The Mendiola massacre", "The EDSA Revolution")
                ));

                questionList.add(new QuizQuestion(
                        "Who was the Filipino general known as 'The Brains of the Revolution'?",
                        "Apolinario Mabini",
                        Arrays.asList("Apolinario Mabini", "Antonio Luna", "Emilio Aguinaldo", "Jose Rizal")
                ));

                questionList.add(new QuizQuestion(
                        "Who became the President of the Philippines after the EDSA People Power Revolution?",
                        "Corazon Aquino",
                        Arrays.asList("Corazon Aquino", "Ferdinand Marcos", "Fidel V. Ramos", "Joseph Estrada")
                ));
                break;
            case "PE":
                questionList = new ArrayList<>();

                questionList.add(new QuizQuestion("What is the national sport of the Philippines?",
                        "Arnis",
                        Arrays.asList("Arnis", "Boxing", "Basketball", "Volleyball")));

                questionList.add(new QuizQuestion("Which Filipino athlete is known as 'The People's Champ'?",
                        "Manny Pacquiao",
                        Arrays.asList("Manny Pacquiao", "Nonito Donaire", "Efren Reyes", "Mark Muñoz")));

                questionList.add(new QuizQuestion("Which dance is traditionally performed in the Philippines involving bamboo sticks?",
                        "Tinikling",
                        Arrays.asList("Tinikling", "Cariñosa", "Pandanggo sa Ilaw", "Maglalatik")));

                questionList.add(new QuizQuestion("Which Filipino athlete won the country's first Olympic gold medal?",
                        "Hidilyn Diaz",
                        Arrays.asList("Hidilyn Diaz", "Manny Pacquiao", "Paeng Nepomuceno", "Carlos Yulo")));

                questionList.add(new QuizQuestion("Which team sport is extremely popular in the Philippines, often played in barangay courts?",
                        "Basketball",
                        Arrays.asList("Basketball", "Volleyball", "Football", "Baseball")));

                questionList.add(new QuizQuestion("Which Filipino billiards player is known as 'The Magician'?",
                        "Efren Reyes",
                        Arrays.asList("Efren Reyes", "Francisco Bustamante", "Alex Pagulayan", "Warren Kiamco")));

                questionList.add(new QuizQuestion("What is the name of the traditional Filipino martial arts?",
                        "Kali",
                        Arrays.asList("Kali", "Judo", "Taekwondo", "Karate")));

                questionList.add(new QuizQuestion("Who is the legendary Filipino bowler known for winning multiple world championships?",
                        "Paeng Nepomuceno",
                        Arrays.asList("Paeng Nepomuceno", "Bong Coo", "Manny Pacquiao", "Efren Reyes")));

                questionList.add(new QuizQuestion("Which beach sport is often played in the Philippines due to its extensive coastlines?",
                        "Beach Volleyball",
                        Arrays.asList("Beach Volleyball", "Surfing", "Beach Soccer", "Kitesurfing")));

                questionList.add(new QuizQuestion("Which Filipino athlete is a world champion gymnast known for winning gold in the World Artistic Gymnastics Championships?",
                        "Carlos Yulo",
                        Arrays.asList("Carlos Yulo", "Manny Pacquiao", "Hidilyn Diaz", "Paeng Nepomuceno")));
                break;
            case "Music":
                questionList.add(new QuizQuestion("Which Filipino musician is known as the 'Father of Kundiman'?",
                        "Nicanor Abelardo", Arrays.asList("Nicanor Abelardo", "Levi Celerio", "Lucio San Pedro", "Francisco Santiago")));

                questionList.add(new QuizQuestion("Who composed the iconic Filipino song 'Anak'?",
                        "Freddie Aguilar", Arrays.asList("Freddie Aguilar", "Ryan Cayabyab", "Rey Valera", "Jose Mari Chan")));

                questionList.add(new QuizQuestion("Which popular Filipino music group is known for the hit 'Spoliarium'?",
                        "Eraserheads", Arrays.asList("Eraserheads", "Parokya ni Edgar", "Rivermaya", "Bamboo")));

                questionList.add(new QuizQuestion("Who is known as the 'Prince of Philippine Jazz'?",
                        "Fred Elizalde", Arrays.asList("Fred Elizalde", "Angel Peña", "Bobby Enriquez", "Boy Katindig")));

                questionList.add(new QuizQuestion("Which famous Filipino conductor founded the Manila Symphony Orchestra?",
                        "Alexander Lippay", Arrays.asList("Alexander Lippay", "Francisco Buencamino", "Antonio Buenaventura", "Felipe Padilla de Leon")));

                questionList.add(new QuizQuestion("Who wrote the lyrics for the Philippine National Anthem?",
                        "José Palma", Arrays.asList("José Palma", "Julian Felipe", "Emilio Aguinaldo", "Apolinario Mabini")));

                questionList.add(new QuizQuestion("Which Filipino pop group gained international fame with their cover of 'Bohemian Rhapsody'?",
                        "Filharmonic", Arrays.asList("Filharmonic", "UP Madrigal Singers", "The Company", "Smokey Mountain")));

                questionList.add(new QuizQuestion("Who is known as the 'Asia's Songbird'?",
                        "Regine Velasquez", Arrays.asList("Regine Velasquez", "Sarah Geronimo", "Lani Misalucha", "Jaya")));

                questionList.add(new QuizQuestion("Which Filipino rapper and hip-hop artist is known for the song 'Lando'?",
                        "Gloc-9", Arrays.asList("Gloc-9", "Francis Magalona", "Andrew E.", "Abra")));

                questionList.add(new QuizQuestion("Which popular Filipino singer is famous for the hit song 'Pagdating ng Panahon'?",
                        "Aiza Seguerra", Arrays.asList("Aiza Seguerra", "Yeng Constantino", "KZ Tandingan", "Moira Dela Torre")));
                break;
            default:

        }


        Collections.shuffle(questionList);

        for (QuizQuestion question : questionList) {
            Collections.shuffle(question.getChoices());
        }
        // Default index to the first question
        currentQuestionIndex = 0;
    }


    // didisplay yung mga questions
    private void displayCurrentQuestion() {
        QuizQuestion currentQuestion = questionList.get(currentQuestionIndex);

        questionText.setText(currentQuestion.getQuestion());
        List<String> choices = currentQuestion.getChoices();

        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));
        choice4.setText(choices.get(3));

        // Reset the selected choice index each time the question changes
        selectedChoiceIndex = -1;
    }

    private void selectChoice(int index) {
        selectedChoiceIndex = index;
        Toast.makeText(this, "Choice " + (index + 1) + " selected", Toast.LENGTH_SHORT).show();
    }

    private void submitAnswer() {
        if (selectedChoiceIndex == -1) {
            Toast.makeText(this, "Please select a choice", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        String selectedAnswer = currentQuestion.getChoices().get(selectedChoiceIndex);

        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            score++; // Increment score on correct answer
        }

        currentQuestionIndex++; // Move to the next question

        if (currentQuestionIndex < questionList.size()) {
            displayCurrentQuestion(); // Show the next question
        } else {
            int totalQuestions = questionList.size();

            // Store the final score with the total number of questions
            saveQuizResult(getIntent().getStringExtra("SUBJECT"), score, totalQuestions);

            // All questions completed, proceed to the score screen
            Intent intent = new Intent(QuizScreen.this, QuickGameScoreScreen.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("TOTAL_QUESTIONS", totalQuestions);
            intent.putExtra("SUBJECT", getIntent().getStringExtra("SUBJECT")); // Pass the subject to the score screen
            startActivity(intent);
            finish(); // Close this activity
        }
    }

    private void saveQuizResult(String subject, int score, int totalQuestions) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Map<String, Object> quizResult = new HashMap<>();
            quizResult.put("score", score);
            quizResult.put("totalQuestions", totalQuestions);

            firestore.collection("quiz_results")
                    .document(userId)
                    .update(subject, FieldValue.arrayUnion(quizResult))
                    .addOnFailureListener(e -> {
                        // If the document doesn't exist, create it with the initial data
                        firestore.collection("quiz_results")
                                .document(userId)
                                .set(Collections.singletonMap(subject, Collections.singletonList(quizResult)))
                                .addOnFailureListener(err -> {
                                    Toast.makeText(this, "Error saving quiz result", Toast.LENGTH_SHORT).show();
                                });
                    });
        }
    }

    class QuizQuestion {
        private final String question;
        private final String correctAnswer;
        private final List<String> choices;

        public QuizQuestion(String question, String correctAnswer, List<String> choices) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.choices = choices;
        }

        public String getQuestion() {
            return question;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public List<String> getChoices() {
            return choices;
        }
    }
}