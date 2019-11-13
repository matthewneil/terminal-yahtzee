import java.util.*;
import java.io.*;

public class Yahtzee {
    public static int[] dice = new int[5];
    public static boolean[] holds = new boolean[5];
    public static Random rand = new Random();
    public static Scanner scan = new Scanner(System.in);
    public static int score, top_score;
    public static boolean used = false;

    /*Categories
    *Values 0 - 5 is Aces, Twos, Threes, Fours, Fives and Sixes (Offset by -1)
    *Value 6 - 3 of a Kind
    *Value 7 - 4 of a Kind
    *Value 8 - Full House
    *Value 9 - Small Straight
    *Value 10 - Large Straight
    *Value 11 - Yahtzee
    *Value 12 - Chance
    */
    public static boolean[] categories = new boolean[13];

    /**release_holds method
     * Used to release all holds
     */
    public static void release_holds() {
        for(int i=0; i< holds.length; i++){
            holds[i] = false;
        }
    }

    /**calculate_number method
     * Method for calculate the values for the basic 1-6 categories
     * Value MUST be between 1, 2, 3, 4, 5 or 6 else this will not work properly
     */
    public static int calculate_number(int num){
        if(categories[num-1] == false){
            return -1;
        }
        if(num <= 0 || num > 6){
            System.out.println("ERROR: Value entered isn't valid! (Must be 1-6)");
        }
        int x = 0;
        for(int i=0; i<5; i++){
            if(dice[i] == num){
                x += num;
            }
        }
        return x;
    }

    /**three_kind method
     * Used to call x_kind method with value '3'
     * Just used so incorrect value isn't inserted into the x_kind method
     */
    public static int three_kind(){
        if(categories[6] == false){
            return -1;
        }
        return x_kind(3);
    }

    /**four_kind method
     * Used to call x_kind method with value '4'
     * Just used so incorrect value isn't inserted into the x_kind method
     */
    public static int four_kind(){
        if(categories[7] == false){
            return -1;
        }
        return x_kind(4);
    }

    /**x_kind method
     * Used for calculating '3 of a Kind' and '4 of a kind'
     * Accepts 3 or 4 as the input parameter
     */
    public static int x_kind(int num) {
        if(num != 3 && num != 4){
            System.out.println("ERROR: Incorrect value inserted into x_kind method! (3 or 4)");
            return 0;
        }
        int x;
        for(int i=1; i<=6; i++){
            x = 0;
            for(int j=0; j<5; j++){
                if(dice[j] == i){
                    x++;
                }
            }
            if(x >= num){
                return total_dice();
            }
        }
        return 0;
    }

    public static int full_house(){
        if(categories[8] == false){
            return -1;
        }
        int x = dice[0];
        int y = 0;

        int f1 = 0;
        int f2 = 0;

        for(int i=0; i<5; i++){//get 2nd value to compare to
            if(dice[i] != x){
                y = dice[i];
                break;
            }
        }

        if(y == 0){//yahtzee condition, so return 0
            return 0;
        }

        for(int i=0; i<5; i++){//check how many of each there are
            if(dice[i] == x){
                f1++;
            }else if(dice[i] == y){
                f2++;
            } else { //if another value is present, full house is not possible, so return 0
                return 0;
            }
        }

        //System.out.println(x + " " +y + " " + f1 + " " + f2);

        if((f1 == 2 && f2 == 3) || (f1 == 3 && f2 == 2)){//full house
            return 25;
        }
        return 0;
    }

    public static int small_str(){
        if(categories[9] == false){
            return -1;
        }
        int[] sorted_dice = dice;
        Arrays.sort(sorted_dice);

        if(sorted_dice[0] > 3){
            return 0;
        }
        
        int length = 1;
        for(int i=0; i<sorted_dice.length-1; i++){

            if((sorted_dice[i] + 1) == sorted_dice[i+1]){
                length++;
            } else if(sorted_dice[i] != sorted_dice[i+1]){
                length = 1;
            }
            if(length == 4){
                return 30;
            }
        }
        return 0;
    }

    public static int large_str(){
        if(categories[10] == false){
            return -1;
        }
        int[] sorted_dice = dice;
        Arrays.sort(sorted_dice);

        if(sorted_dice[0] > 2){
            return 0;
        }
        
        int length = 1;
        for(int i=0; i<sorted_dice.length-1; i++){

            if((sorted_dice[i] + 1) == sorted_dice[i+1]){
                length++;
            } else if(sorted_dice[i] != sorted_dice[i+1]){
                length = 1;
            }
            if(length == 5){
                return 40;
            }
        }
        return 0;
    }

    public static int yahtzee(boolean commit){
        int x = dice[0];
        for(int i=0; i<dice.length; i++){
            if(dice[i] != x){
                if(categories[11] == false){
                    return -1;
                }
                if(commit){
                    used = true;
                }
                return 0;
            }
        }
        if(used){
            return -1;
        }
        if(categories[11] == false){
            return 100;
        }
        return 50;
    }

    public static int chance(){
        if(categories[12] == false){
            return -1;
        }
        return total_dice();
    }

    public static int total_dice(){
        int x = 0;
        for(int i=0; i<5; i++){
            x += dice[i];
        }
        return x;
    }

    public static void roll_dice(boolean p){
        for(int i=0; i<dice.length; i++){
            if(holds[i] == false){
                dice[i] = rand.nextInt(6) + 1;
            }
        }
        if(p){
            print_dice();  
            //System.out.println("Dice 1: " + dice[0] + "\nDice 2: " + dice[1] + "\nDice 3: " + dice[2] + "\nDice 4: " + dice[3] + "\nDice 5: " + dice[4]); //old print statement
        }
    }

    public static int set_holds(String holds_str){
        holds_str = holds_str.trim();
        /* //Old formatting
        if(holds_str.equals("")){
            System.out.println("No value was inserted, please enter which dice you want to hold. (Refer to Format Below)");
            return -1;
        }
        if(holds_str.length() != 5){
            System.out.println("Inserted value was Too Short or Too long, should be 5 letters long. (Refer to Format Below)");
            return -1;
        }
        for(int i=0; i<holds.length; i++){
            if(holds_str.charAt(i) == 'x'){
                holds[i] = true;
            } else if(holds_str.charAt(i) == 'o'){
                holds[i] = false;
            } else {
                System.out.println("Value contained characters that weren't 'x' or 'o'.  (Refer to Format Below)");
                return -1;
            }
        }*/
        release_holds();
        for(int i=0; i<holds_str.length(); i++){
            if(holds_str.charAt(i) == '1'){
                holds[0] = true;
            } else if(holds_str.charAt(i) == '2'){
                holds[1] = true;
            } else if(holds_str.charAt(i) == '3'){
                holds[2] = true;
            } else if(holds_str.charAt(i) == '4'){
                holds[3] = true;
            } else if(holds_str.charAt(i) == '5'){
                holds[4] = true;
            } else {
                System.out.println("Entered number contained numbers values that weren't 1-5  (Refer to Format Below)");
                return -1;
            }
        }
        return 1;
    }
    public static void dice_phase(){
        String message = "Set which dice you want to hold and press enter to roll!\n(Format: '14' will hold dice 1 and 4)";
        roll_dice(true);
        System.out.println(message);
        String scan_holds;
        int success;
        while(true){
            scan_holds = scan.nextLine();
            success = set_holds(scan_holds);
            if(success == 1){
                break;
            }
            System.out.println("(Format: '14' will hold dice 1 and 4)");
            release_holds();
        }

        roll_dice(true);
        System.out.println(message);
        while(true){
            scan_holds = scan.nextLine();
            success = set_holds(scan_holds);
            if(success == 1){
                break;
            }
            System.out.println("(Format: '14' will hold dice 1 and 4)");
            release_holds();
        }

        set_holds(scan_holds);

        roll_dice(true);
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static void selection_phase(){ 
        int[] scores = {calculate_number(1), calculate_number(2), calculate_number(3), calculate_number(4), calculate_number(5), calculate_number(6),
                        three_kind(), four_kind(), full_house(), small_str(), large_str(), yahtzee(false), chance()};
        String[] scores_str = new String[13];
        for(int i=0; i<scores.length; i++){
            if(scores[i] == -1){
                scores_str[i] = "N/A";
            } else {
                scores_str[i] = Integer.toString(scores[i]);
            }
        }
        System.out.format("%s %s\t%s %s\n", "(1)Aces:", scores_str[0], "(7)Three of a Kind:", scores_str[6]);
        System.out.format("%s %s\t%s %s\n", "(2)Twos:", scores_str[1], "(8)Four of a Kind:", scores_str[7]);
        System.out.format("%s %s\t%s %s\n", "(3)Threes:", scores_str[2], "(9)Full House:", scores_str[8]);
        System.out.format("%s %s\t%s %s\n", "(4)Fours:", scores_str[3], "(10)Small Straight:", scores_str[9]);
        System.out.format("%s %s\t%s %s\n", "(5)Fives:", scores_str[4], "(11)Large Straight:", scores_str[10]);
        System.out.format("%s %s\t%s %s\n", "(6)Sixes:", scores_str[5], "(12)Yahtzee!:", scores_str[11]);
        System.out.format("\t\t%s %s\n", "(13)Chance:", scores_str[12]);

        System.out.println("Enter the (number) that corresponds to the Category you want.");
        int cate = 0;
        while(true){
            String cate_str = scan.nextLine();
            if(!isNumeric(cate_str)){
                System.out.println("Value entered wasn't a number!");
            } else {
                cate = Integer.parseInt(cate_str);
                if(cate < 1 || cate > 13){
                    System.out.println("Number entered wasn't a selectable category!");
                } else {
                    if(scores[cate-1] == -1){
                        System.out.println("You cannot select this category as it has already been used!");
                    } else {
                        break;
                    }
                }
            }
        }
        if(cate == 0){
            System.err.println("ERROR: If this message appears something has gone wrong.");
        }

        if(cate > 0 && cate <= 6){
            score += scores[cate-1];
            top_score += scores[cate-1];
        } else if ((cate >= 7 && cate <= 11 )|| cate == 13){
            score += scores[cate-1];
        } else if (cate == 12){
            score += yahtzee(true);
        }        
        remove_category(cate);
    }

    public static void remove_category(int cate){
        categories[cate-1] = false;
    }

    public static void print_dice(){
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!!!!!!!!!!!!! ");
            } else if(dice[i] == 2){
                System.out.print("@@@@@@@@@@@@@@ ");
            } else if(dice[i] == 3){
                System.out.print("############## ");
            } else if(dice[i] == 4){
                System.out.print("$$$$$$$$$$$$$$ ");
            } else if(dice[i] == 5){
                System.out.print("%%%%%%%%%%%%%% ");
            } else if(dice[i] == 6){
                System.out.print("&&&&&&&&&&&&&& ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!          !! ");
            } else if(dice[i] == 2){
                System.out.print("@@          @@ ");
            } else if(dice[i] == 3){
                System.out.print("##          ## ");
            } else if(dice[i] == 4){
                System.out.print("$$          $$ ");
            } else if(dice[i] == 5){
                System.out.print("%%          %% ");
            } else if(dice[i] == 6){
                System.out.print("&&          && ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!          !! ");
            } else if(dice[i] == 2){
                System.out.print("@@      ()  @@ ");
            } else if(dice[i] == 3){
                System.out.print("##      ()  ## ");
            } else if(dice[i] == 4){
                System.out.print("$$  ()  ()  $$ ");
            } else if(dice[i] == 5){
                System.out.print("%%  ()  ()  %% ");
            } else if(dice[i] == 6){
                System.out.print("&&  ()  ()  && ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!    ()    !! ");
            } else if(dice[i] == 2){
                System.out.print("@@          @@ ");
            } else if(dice[i] == 3){
                System.out.print("##    ()    ## ");
            } else if(dice[i] == 4){
                System.out.print("$$          $$ ");
            } else if(dice[i] == 5){
                System.out.print("%%    ()    %% ");
            } else if(dice[i] == 6){
                System.out.print("&&  ()  ()  && ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!          !! ");
            } else if(dice[i] == 2){
                System.out.print("@@  ()      @@ ");
            } else if(dice[i] == 3){
                System.out.print("##  ()      ## ");
            } else if(dice[i] == 4){
                System.out.print("$$  ()  ()  $$ ");
            } else if(dice[i] == 5){
                System.out.print("%%  ()  ()  %% ");
            } else if(dice[i] == 6){
                System.out.print("&&  ()  ()  && ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!          !! ");
            } else if(dice[i] == 2){
                System.out.print("@@          @@ ");
            } else if(dice[i] == 3){
                System.out.print("##          ## ");
            } else if(dice[i] == 4){
                System.out.print("$$          $$ ");
            } else if(dice[i] == 5){
                System.out.print("%%          %% ");
            } else if(dice[i] == 6){
                System.out.print("&&          && ");
            }
        }
        System.out.println();
        for(int i=0; i<dice.length; i++){
            if(dice[i] == 1){
                System.out.print("!!!!!!!!!!!!!! ");
            } else if(dice[i] == 2){
                System.out.print("@@@@@@@@@@@@@@ ");
            } else if(dice[i] == 3){
                System.out.print("############## ");
            } else if(dice[i] == 4){
                System.out.print("$$$$$$$$$$$$$$ ");
            } else if(dice[i] == 5){
                System.out.print("%%%%%%%%%%%%%% ");
            } else if(dice[i] == 6){
                System.out.print("&&&&&&&&&&&&&& ");
            }
        }
        System.out.println();
    }

    
    public static void main(String[] args){
        System.out.println("Command Line Yahtzee!");
        int highscore = 0;
        

        
        for(int i=0; i<categories.length; i++){
            categories[i] = true;
        }
        boolean cont = false;
        score = 0;
        System.out.println("Highscore: " + highscore);
        while(true){
            cont = false;
            release_holds();       
            dice_phase();
            selection_phase();
            System.out.println("Score: " + score + "\n");
            for(int i=0; i<categories.length; i++){
                if(categories[i] == true){
                    cont = true;
                }
            }
            if(!cont){
                break;
            }
        }
        if(top_score >= 63){
            System.out.println("Your Upper Section got 63 or above score! You get a 35 score bonus!");
            score += 35;
        }
        System.out.println("Highscore: " + highscore);
        if(score > highscore){
            System.out.println("Congratulations! You got a new highscore!");
        }
        System.out.println("Game Finished!\nYour score was " + score + "!");

    }
}
 /*
        dice[0] = 4;
        dice[1] = 4;
        dice[2] = 4;
        dice[3] = 3;
        dice[4] = 4;
        System.out.println(yahtzee());*/