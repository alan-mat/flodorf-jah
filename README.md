# Jokes Against Humanity

Currently, no view implemented.

### How to use cli prototype:

At least 2 players required. All players must be in the same network. The game can be
started using within the MainController class (main function). *Multiple clients can
be started on the same machine!* One of the players has to host the game, the others can 
join using the host's IP address. After entering their name a player can immediately
start sending and receiving chat messages. Entering *exit*, *quit* or *disconnect* closes
the client. 

To actually start the game all players should enter the command **!ready**. The game starts,
when all players are ready. Each round all players will receive a prompt (*currently stolen
from cards against humanity*) to which they should answer with the funniest response. 
After the timer runs out, all answers are shown to the players (anonymously) and now the 
players vote for the answer they find the best. The best answers are rewarded points
appropriately. Following commands are used to send an answer or vote to the server:

- **!answer** <*write your answer here*>
- **!vote** <*number of answer you find the best*>

The game ends when one of the players reaches the point threshold. 
