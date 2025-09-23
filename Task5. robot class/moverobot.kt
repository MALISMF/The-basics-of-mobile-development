fun moveRobot(r: Robot, toX: Int, toY: Int) {
    fun turnToDirection(robot: Robot, targetDirection: Direction) {
        while (robot.direction != targetDirection) {
            robot.turnRight()
        }
    }
    
    //  по оси X
    val deltaX = toX - r.x
    if (deltaX > 0) {
        turnToDirection(r, Direction.RIGHT)
        repeat(deltaX) { r.stepForward() }
    } else if (deltaX < 0) {
        turnToDirection(r, Direction.LEFT)
        repeat(-deltaX) { r.stepForward() }
    }
    
    //  по оси Y
    val deltaY = toY - r.y
    if (deltaY > 0) {
        turnToDirection(r, Direction.UP)
        repeat(deltaY) { r.stepForward() }
    } else if (deltaY < 0) {
        turnToDirection(r, Direction.DOWN)
        repeat(-deltaY) { r.stepForward() }
    }
}