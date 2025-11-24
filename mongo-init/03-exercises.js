db = db.getSiblingDB("WORKOUT_DB");

db.exercises.insertMany([
  {
    name: "Push-up",
    muscle: "Chest",
    difficulty: "Beginner",
    instructions: "Start in plank position, lower your body, push up."
  },
  {
    name: "Squat",
    muscle: "Legs",
    difficulty: "Beginner",
    instructions: "Stand with feet shoulder-width apart, lower down, then stand up."
  },
  {
    name: "Plank",
    muscle: "Core",
    difficulty: "Beginner",
    instructions: "Hold your body straight in plank position for 30-60 seconds."
  }
]);
