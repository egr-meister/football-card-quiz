package com.footballcardquiz.app.data.local

import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.data.model.QuizQuestion

/**
 * Local, offline question bank for Football Card Quiz.
 *
 * All questions are original and educational. They intentionally use NO real
 * player names, NO official club names, and NO official league branding.
 * Content is simplified for learning and is not an official rules source.
 *
 * Counts: 18 Easy, 18 Medium, 18 Hard = 54 total (spec minimum is 45).
 */
object QuestionBank {

    private fun q(
        id: String,
        category: QuizCategory,
        difficulty: QuizDifficulty,
        question: String,
        answers: List<String>,
        correct: Int,
        explanation: String
    ) = QuizQuestion(id, category, difficulty, question, answers, correct, explanation)

    val allQuestions: List<QuizQuestion> = buildList {

        // ================= EASY (18) =================
        add(q("e01", QuizCategory.GeneralFootball, QuizDifficulty.Easy,
            "How many players from one team are on the pitch at kickoff?",
            listOf("9", "10", "11", "12"), 2,
            "A team starts a match with eleven players, including the goalkeeper."))
        add(q("e02", QuizCategory.GeneralFootball, QuizDifficulty.Easy,
            "How long is a standard football match (two halves), not counting stoppage time?",
            listOf("60 minutes", "80 minutes", "90 minutes", "120 minutes"), 2,
            "Two halves of 45 minutes each make 90 minutes of regular play."))
        add(q("e03", QuizCategory.RefereeCards, QuizDifficulty.Easy,
            "What does a yellow card usually mean?",
            listOf("A goal is disallowed", "A caution / warning", "The match is over", "A substitution"), 1,
            "A yellow card is a caution. Two yellows in a match become a red card."))
        add(q("e04", QuizCategory.RefereeCards, QuizDifficulty.Easy,
            "What happens when a player receives a red card?",
            listOf("Nothing changes", "They are sent off the pitch", "They get a penalty", "They swap positions"), 1,
            "A red card means the player is dismissed and the team continues short-handed."))
        add(q("e05", QuizCategory.PlayerPositions, QuizDifficulty.Easy,
            "Which player is allowed to use their hands inside their own penalty area?",
            listOf("Defender", "Midfielder", "Goalkeeper", "Forward"), 2,
            "Only the goalkeeper may handle the ball, and only within their own penalty area."))
        add(q("e06", QuizCategory.PlayerPositions, QuizDifficulty.Easy,
            "What is the main job of a forward (striker)?",
            listOf("Stop shots", "Score goals", "Throw the ball in", "Keep the clock"), 1,
            "Forwards focus mainly on attacking and scoring goals."))
        add(q("e07", QuizCategory.FootballRules, QuizDifficulty.Easy,
            "How is play restarted after the ball fully crosses the sideline (touchline)?",
            listOf("Corner kick", "Goal kick", "Throw-in", "Penalty"), 2,
            "When the ball leaves over the touchline, play restarts with a throw-in."))
        add(q("e08", QuizCategory.FootballRules, QuizDifficulty.Easy,
            "What is it called when the ball fully crosses the goal line between the posts, under the bar?",
            listOf("A corner", "A goal", "A foul", "A save"), 1,
            "A goal is scored when the whole ball crosses the goal line inside the frame."))
        add(q("e09", QuizCategory.MatchSituations, QuizDifficulty.Easy,
            "What restart is given when the attacking team last touches the ball before it crosses their opponents' goal line (not a goal)?",
            listOf("Corner kick", "Goal kick", "Throw-in", "Free kick"), 1,
            "If an attacker puts it over the goal line, the defending team gets a goal kick."))
        add(q("e10", QuizCategory.MatchSituations, QuizDifficulty.Easy,
            "From where is a penalty kick taken?",
            listOf("The center circle", "The penalty spot", "The corner arc", "The halfway line"), 1,
            "A penalty kick is taken from the penalty spot with only the goalkeeper defending."))
        add(q("e11", QuizCategory.FieldAndEquipment, QuizDifficulty.Easy,
            "What shape is a standard football pitch?",
            listOf("Circle", "Rectangle", "Triangle", "Hexagon"), 1,
            "A football pitch is rectangular, longer than it is wide."))
        add(q("e12", QuizCategory.FieldAndEquipment, QuizDifficulty.Easy,
            "What protective equipment is worn on the lower legs?",
            listOf("Elbow pads", "Shin guards", "Gloves", "Helmet"), 1,
            "Outfield players wear shin guards for lower-leg protection."))
        add(q("e13", QuizCategory.GeneralFootball, QuizDifficulty.Easy,
            "What is the maximum number of players a team can have on the pitch at once?",
            listOf("10", "11", "12", "13"), 1,
            "A team may have at most eleven players on the pitch at any time."))
        add(q("e14", QuizCategory.RefereeCards, QuizDifficulty.Easy,
            "How many yellow cards in one match lead to a red card?",
            listOf("One", "Two", "Three", "Four"), 1,
            "Two yellow cards in the same match result in a red card and a send-off."))
        add(q("e15", QuizCategory.FootballRules, QuizDifficulty.Easy,
            "Which restart is awarded from the corner arc?",
            listOf("Throw-in", "Corner kick", "Goal kick", "Penalty"), 1,
            "A corner kick is taken from the corner arc after the defending team plays it out over their own goal line."))
        add(q("e16", QuizCategory.PlayerPositions, QuizDifficulty.Easy,
            "Which position mainly links defense and attack?",
            listOf("Goalkeeper", "Midfielder", "Center-back", "Full-back"), 1,
            "Midfielders connect defense and attack, both supporting and building play."))
        add(q("e17", QuizCategory.MatchSituations, QuizDifficulty.Easy,
            "What signals the start of each half of play?",
            listOf("A throw-in", "A kickoff", "A corner", "A goal kick"), 1,
            "Each half begins with a kickoff from the center of the pitch."))
        add(q("e18", QuizCategory.FieldAndEquipment, QuizDifficulty.Easy,
            "What are the two upright posts and the top bar of the goal collectively called?",
            listOf("The net", "The frame (posts and crossbar)", "The arc", "The box"), 1,
            "The goal frame is made of two goalposts and a crossbar."))

        // ================= MEDIUM (18) =================
        add(q("m01", QuizCategory.FootballRules, QuizDifficulty.Medium,
            "In simple terms, when is an attacker in an offside position?",
            listOf(
                "Anywhere in the opponents' half",
                "Nearer the opponents' goal line than the ball and the second-to-last defender when the ball is played",
                "Only inside the penalty area",
                "Whenever they are behind the ball"
            ), 1,
            "Offside position means being closer to the goal line than both the ball and the second-to-last defender at the moment a teammate plays the ball."))
        add(q("m02", QuizCategory.MatchSituations, QuizDifficulty.Medium,
            "A defender deliberately handles the ball to stop a clear goal. What is the typical outcome?",
            listOf("Corner and yellow card", "Free kick or penalty and possible red card", "Goal kick only", "Nothing"), 1,
            "Denying an obvious goal by deliberate handball is a serious foul that can bring a penalty (or free kick) and a red card."))
        add(q("m03", QuizCategory.RefereeCards, QuizDifficulty.Medium,
            "Which of these is most likely to earn a yellow card?",
            listOf("A clean tackle winning the ball", "Persistent fouling or unsporting behavior", "Passing back to the keeper", "Taking a throw-in"), 1,
            "Repeated fouls or unsporting behavior are common reasons for a caution."))
        add(q("m04", QuizCategory.PlayerPositions, QuizDifficulty.Medium,
            "What does a full-back primarily do?",
            listOf("Play as the main striker", "Defend the wide areas and support attacks down the flank", "Take all corner kicks", "Only defend set pieces"), 1,
            "Full-backs defend the wide defensive areas and often push forward to support wing play."))
        add(q("m05", QuizCategory.FootballRules, QuizDifficulty.Medium,
            "What is the difference between a direct and an indirect free kick?",
            listOf(
                "There is no difference",
                "A goal can be scored directly from a direct free kick; an indirect one needs another touch first",
                "Indirect kicks are always penalties",
                "Direct kicks must be passed backward"
            ), 1,
            "You can score straight from a direct free kick. From an indirect free kick, another player must touch the ball before a goal counts."))
        add(q("m06", QuizCategory.MatchSituations, QuizDifficulty.Medium,
            "During a penalty kick, where must the goalkeeper be as the kick is taken?",
            listOf("Anywhere in the box", "On or in front of the goal line, between the posts", "Next to the penalty spot", "Off the pitch"), 1,
            "The goalkeeper must stay on the goal line between the posts until the ball is kicked, with at least part of one foot on or in line with it."))
        add(q("m07", QuizCategory.FieldAndEquipment, QuizDifficulty.Medium,
            "What is the curved line at the top edge of the penalty area called?",
            listOf("The center circle", "The penalty arc (D)", "The corner arc", "The goal area line"), 1,
            "The penalty arc, sometimes called the 'D', keeps players the required distance from the penalty spot."))
        add(q("m08", QuizCategory.GeneralFootball, QuizDifficulty.Medium,
            "What is 'stoppage time' (added time)?",
            listOf("Halftime break", "Extra minutes added to make up for stoppages during a half", "A penalty shootout", "A timeout called by coaches"), 1,
            "The referee adds time at the end of each half to compensate for stoppages like injuries and substitutions."))
        add(q("m09", QuizCategory.PlayerPositions, QuizDifficulty.Medium,
            "What is a center-back mainly responsible for?",
            listOf("Scoring most goals", "Central defending and marking attackers", "Taking throw-ins", "Managing the clock"), 1,
            "Center-backs defend centrally, block shots, and mark opposing forwards."))
        add(q("m10", QuizCategory.RefereeCards, QuizDifficulty.Medium,
            "Serious foul play or violent conduct typically results in which card?",
            listOf("Yellow", "Green", "Red", "No card"), 2,
            "Violent conduct or serious foul play is punished with a direct red card and a send-off."))
        add(q("m11", QuizCategory.FootballRules, QuizDifficulty.Medium,
            "Can a player be penalized for offside directly from a throw-in?",
            listOf("Yes, always", "No, there is no offside directly from a throw-in", "Only in the second half", "Only for the goalkeeper"), 1,
            "There is no offside offense if the ball is received directly from a throw-in."))
        add(q("m12", QuizCategory.MatchSituations, QuizDifficulty.Medium,
            "The goalkeeper picks up a deliberate back-pass kicked by a teammate. What is awarded?",
            listOf("Penalty kick", "Indirect free kick to the opponents", "Corner kick", "Nothing"), 1,
            "A keeper handling a deliberate kicked back-pass concedes an indirect free kick."))
        add(q("m13", QuizCategory.FieldAndEquipment, QuizDifficulty.Medium,
            "What is the small box directly in front of the goal called?",
            listOf("Penalty area", "Goal area (six-yard box)", "Center circle", "Technical area"), 1,
            "The goal area, often called the six-yard box, is the smaller box used for goal kicks and certain restarts."))
        add(q("m14", QuizCategory.GeneralFootball, QuizDifficulty.Medium,
            "What is a 'clean sheet'?",
            listOf("A new match ball", "A game where a team concedes no goals", "A fresh set of kits", "A tied score"), 1,
            "A clean sheet means a team finished the match without conceding any goals."))
        add(q("m15", QuizCategory.PlayerPositions, QuizDifficulty.Medium,
            "A defensive midfielder mainly does what?",
            listOf("Guards the space in front of the defense and breaks up attacks", "Only takes penalties", "Plays as an extra striker", "Stands on the goal line"), 0,
            "A defensive midfielder shields the back line and disrupts opposition attacks."))
        add(q("m16", QuizCategory.FootballRules, QuizDifficulty.Medium,
            "Can a goal be scored directly from a corner kick?",
            listOf("No, never", "Yes, directly against the opponents", "Only after two touches", "Only in extra time"), 1,
            "A goal may be scored directly from a corner kick against the defending team."))
        add(q("m17", QuizCategory.RefereeCards, QuizDifficulty.Medium,
            "Which action is most likely 'unsporting behavior' worthy of a yellow card?",
            listOf("Cleanly heading the ball", "Simulation (diving) to fool the referee", "Taking a normal throw-in", "Making a legal substitution"), 1,
            "Simulation, or diving to deceive the referee, is unsporting behavior and cautionable."))
        add(q("m18", QuizCategory.MatchSituations, QuizDifficulty.Medium,
            "What is the advantage rule?",
            listOf(
                "The referee always stops play for any foul",
                "The referee lets play continue if stopping it would benefit the fouling team",
                "The attacking team gets an extra player",
                "The clock is paused"
            ), 1,
            "Under advantage, the referee allows play to continue when a stoppage would help the team that committed the foul."))

        // ================= HARD (18) =================
        add(q("h01", QuizCategory.FootballRules, QuizDifficulty.Hard,
            "Being in an offside position is only an offense when the player does what?",
            listOf(
                "Stands still",
                "Becomes involved in active play (e.g. interfering with play or an opponent)",
                "Is in their own half",
                "Wears the wrong boots"
            ), 1,
            "Offside is only penalized when the player in an offside position becomes actively involved in the play."))
        add(q("h02", QuizCategory.MatchSituations, QuizDifficulty.Hard,
            "If the ball strikes the referee and a promising attack results, what is the usual restart under current guidance?",
            listOf("Play simply continues", "A dropped ball is awarded", "A penalty is given", "A corner is given"), 1,
            "When the ball hits the referee and possession/attack is affected, play is restarted with a dropped ball."))
        add(q("h03", QuizCategory.RefereeCards, QuizDifficulty.Hard,
            "A player already on a yellow card commits another cautionable offense. What should happen?",
            listOf("A quiet warning", "A second yellow, then a red, and a send-off", "A straight penalty", "Nothing until halftime"), 1,
            "A second caution leads to a second yellow followed by a red card, and the player is sent off."))
        add(q("h04", QuizCategory.PlayerPositions, QuizDifficulty.Hard,
            "What best describes a 'sweeper' role?",
            listOf(
                "A striker who presses high",
                "A free defender behind the main defensive line to clear loose balls",
                "A winger who stays wide",
                "A goalkeeper who never leaves the line"
            ), 1,
            "A sweeper plays behind the defensive line, sweeping up balls that get past the other defenders."))
        add(q("h05", QuizCategory.FootballRules, QuizDifficulty.Hard,
            "From a goalkeeper's throw or kick, can an attacker be offside if they receive it in the opponents' half?",
            listOf(
                "Offside is judged the same as any teammate's pass",
                "Offside never applies to a goalkeeper's distribution",
                "Only if it is a throw",
                "Only during set pieces"
            ), 0,
            "A goalkeeper is a teammate for offside purposes, so normal offside judgment applies to their distribution in open play."))
        add(q("h06", QuizCategory.MatchSituations, QuizDifficulty.Hard,
            "A defender commits a foul that denies an obvious goal-scoring opportunity but makes a genuine attempt to play the ball inside the penalty area. What is common under current guidance?",
            listOf(
                "Automatic red card",
                "Penalty plus a yellow card (caution) rather than a red",
                "Corner kick only",
                "No action"
            ), 1,
            "For a genuine attempt to play the ball that denies a goal chance in the box, the offense is often a penalty with a caution rather than a send-off."))
        add(q("h07", QuizCategory.FieldAndEquipment, QuizDifficulty.Hard,
            "How far must defenders be from the ball at a corner kick or free kick (minimum distance)?",
            listOf("Any distance", "About 9.15 meters (10 yards)", "About 5 meters", "About 20 meters"), 1,
            "Opponents must retreat at least 9.15 meters (10 yards) from the ball at most free kicks and corners."))
        add(q("h08", QuizCategory.GeneralFootball, QuizDifficulty.Hard,
            "In a two-legged knockout tie without away-goal tiebreakers, how is a level aggregate usually decided?",
            listOf("Coin toss", "Extra time and, if needed, a penalty shootout", "Replay the whole tie", "Both teams advance"), 1,
            "A level aggregate is typically settled by extra time and then a penalty shootout if still level."))
        add(q("h09", QuizCategory.PlayerPositions, QuizDifficulty.Hard,
            "What is an 'inverted' full-back?",
            listOf(
                "A full-back who plays in goal",
                "A full-back who moves into central midfield areas when attacking",
                "A striker who defends",
                "A defender who only heads the ball"
            ), 1,
            "An inverted full-back tucks into central midfield areas in possession to help build play."))
        add(q("h10", QuizCategory.FootballRules, QuizDifficulty.Hard,
            "At a penalty kick, if an attacking teammate encroaches into the area early and their team scores, what is the typical outcome?",
            listOf("Goal stands", "The kick is retaken", "A goal kick", "A red card"), 1,
            "Encroachment by the kicker's team when a goal is scored normally leads to a retake."))
        add(q("h11", QuizCategory.RefereeCards, QuizDifficulty.Hard,
            "Denying an obvious goal-scoring opportunity by a foul OUTSIDE the penalty area usually results in what?",
            listOf("Yellow card and penalty", "Red card and a direct free kick", "Corner kick", "Indirect free kick only"), 1,
            "A foul that denies a clear goal chance outside the box is normally a red card with a direct free kick."))
        add(q("h12", QuizCategory.MatchSituations, QuizDifficulty.Hard,
            "During a penalty shootout, can a goalkeeper who is injured be replaced if a substitution is still available?",
            listOf("No, never", "Yes, a named substitute may replace an injured keeper if a sub remains", "Only an outfield player can go in goal", "Only before the shootout starts"), 1,
            "If a substitution slot remains, an injured goalkeeper can be replaced during the shootout."))
        add(q("h13", QuizCategory.FieldAndEquipment, QuizDifficulty.Hard,
            "What is the diameter of the center circle on a standard pitch?",
            listOf("5 meters", "About 9.15 meters radius (roughly 18.3 m across)", "30 meters across", "2 meters"), 1,
            "The center circle has a radius of 9.15 meters, keeping opponents back at kickoff."))
        add(q("h14", QuizCategory.FootballRules, QuizDifficulty.Hard,
            "Can a player score an own goal directly from a throw-in they take?",
            listOf(
                "Yes, it counts as an own goal",
                "No; if it goes straight into their own goal, a corner is awarded to the opponents",
                "It is a penalty",
                "It is a goal kick"
            ), 1,
            "A ball thrown directly into one's own goal is not a goal; the opponents receive a corner kick."))
        add(q("h15", QuizCategory.PlayerPositions, QuizDifficulty.Hard,
            "What is a 'false nine'?",
            listOf(
                "A defender wearing number nine",
                "A central forward who drops deep into midfield to create space and chances",
                "A substitute striker",
                "A second goalkeeper"
            ), 1,
            "A false nine is a nominal striker who drops into midfield, dragging defenders and opening space."))
        add(q("h16", QuizCategory.MatchSituations, QuizDifficulty.Hard,
            "If the ball bursts or becomes defective during open play, how is the match usually restarted?",
            listOf("Corner kick", "A dropped ball with a new ball", "Penalty", "Throw-in"), 1,
            "When the ball becomes defective in open play, the match restarts with a dropped ball using a replacement ball."))
        add(q("h17", QuizCategory.GeneralFootball, QuizDifficulty.Hard,
            "How many officials are typically on the field and its edges in a standard professional match (excluding video review)?",
            listOf("One", "Four (referee, two assistants, fourth official)", "Ten", "Two"), 1,
            "A typical match has a referee, two assistant referees, and a fourth official."))
        add(q("h18", QuizCategory.RefereeCards, QuizDifficulty.Hard,
            "Which offense is most clearly a straight red card rather than a yellow?",
            listOf("A late but non-violent tackle", "Spitting at an opponent", "Time-wasting", "Kicking the ball away once"), 1,
            "Spitting at another person is violent/serious misconduct and is a straight red card offense."))
    }

    /**
     * Build a quiz set for the given difficulty and optional category.
     * Never crashes: falls back to broader pools if the exact filter is empty.
     * Returns a shuffled, size-capped list; may be shorter than [count] if the
     * bank does not have enough matching questions.
     */
    fun buildQuiz(
        difficulty: QuizDifficulty,
        category: QuizCategory?,
        count: Int
    ): List<QuizQuestion> {
        val valid = allQuestions.filter { it.isValid }

        // 1) exact difficulty + category
        var pool = valid.filter {
            it.difficulty == difficulty && (category == null || it.category == category)
        }
        // 2) fall back to difficulty only
        if (pool.isEmpty()) pool = valid.filter { it.difficulty == difficulty }
        // 3) fall back to category only
        if (pool.isEmpty() && category != null) pool = valid.filter { it.category == category }
        // 4) fall back to everything
        if (pool.isEmpty()) pool = valid

        return pool.shuffled().take(count.coerceAtLeast(1))
    }

    fun countFor(difficulty: QuizDifficulty, category: QuizCategory?): Int =
        allQuestions.count {
            it.isValid && it.difficulty == difficulty && (category == null || it.category == category)
        }
}
