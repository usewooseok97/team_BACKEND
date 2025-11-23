db = db.getSiblingDB("WORKOUT_DB"); 

db.users.insertMany([
  {
    username: "admin",
    password: "admin123",
    role: "admin",
    createdAt: new Date()
  },
  {
    username: "user1",
    password: "user123",
    role: "user",
    createdAt: new Date()
  }
]);
