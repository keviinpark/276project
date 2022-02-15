class ldr_entry{
    int score;
    int ranking;
    int total_ticks;
    String name;
}

class leaderboard{
    //this array will hold the top ten leaderboard entries
    static ldr_entry[] top_ten = new ldr_entry[10];

    //this will keep track of how many entries are in the leaderboard
    static int count = 0;

    static void add_leaderboard(ldr_entry high_score){
        //confirm if the new score is actually a high_score
        //in the case of identical score, player with lower tick count will be ranked higher
        //so total_ticks will be the tiebreaker

        //Case A: The leaderboard is not yet full, and thus the user's score will for sure make it 
        //into the leaderboard
        if (count <= 9){
            top_ten[count] = high_score;
            count ++;
        }
        //Case B: The leaderboard is full, but the user's score is a top ten score and thus will
        //be recorded into the leaderboard
        else if (high_score.score >= leaderboard.top_ten[9].score) {

            //i is just an incrementor variable
            //will return the index the user's score should occupy in the top_ten array
            int i = 0;
            while (high_score.score <= leaderboard.top_ten[i].score){
                if (high_score.score == leaderboard.top_ten[i].score){

                    if (high_score.total_ticks < leaderboard.top_ten[i].total_ticks){
                        i++;
                    }
                    else{
                        break;
                    }
                }
                else{
                    i++;
                }
                
            }
            //shift down the existing scores below user's score by one position, provided the user's score is not 
            //in position 10, or index 9. In that case, no shifting is necessary
            if (i < 9){
                for (int j = 8; j >= i; j--){
                    leaderboard.top_ten[j+1] = leaderboard.top_ten[j];
                }
            }

            //maybe we can take input here for a prefereed player nickname, since at this stage, it is established
            //that we have a new leaderboard entry

            //PSEUDOCODE:
            // high_score.name = USER_INPUT (as a string)

            leaderboard.top_ten[i] = high_score;
            high_score.ranking = i;
            count = 10;
        }
        
        else{
            //user's score was not a high score
            count = 10;
        }
        
        
    }
}

