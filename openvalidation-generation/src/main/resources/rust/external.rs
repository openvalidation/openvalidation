pub(crate) mod external;

pub(crate) struct MyData {
    name: String,
    age: u32,
}


pub(crate) struct MyData2 {
    amount: u8,
    location: Point,
}

pub(crate) struct Point {
    x: f32,
    y: f32,
}

pub(crate) struct MyData3 {
    amount: u8,
    location: Point,
    curve: Vec<Point>,
}