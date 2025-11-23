db = db.getSiblingDB("WORKOUT_DB");

db.products.insertMany([
  {
    brand: "Nike",
    name: "Running Shoes",
    originalPrice: 150,
    price: 120,
    image: "nike_shoes.png"
  },
  {
    brand: "Adidas",
    name: "Yoga Mat",
    originalPrice: 50,
    price: 40,
    image: "adidas_mat.png"
  }
]);
