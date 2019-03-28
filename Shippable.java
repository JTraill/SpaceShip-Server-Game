public interface Shippable  
{
   int PORT = 2018;
   int START = 0;
   int STOP = 1; 
   int QUIT = 2; 
   int POST = 3; 
   int DONE = 4;
   int GETSCORES = 5;
   int NAME = 6;
   String[] CMD = 
   {
      "START",    // 0    server -> client, one int arg
      "STOP", // 1    server -> client, one string arg
      "QUIT", // 2   client -> server
      "POST", // 3    server -> client, list of past and current score
      "DONE", //4      server -> server, done command
      "GETSCORES" //5 Server -> client get score command
   };

   /**
   * 
   *    START      server  &rarr; client, one int
   *    STOP   server  &rarr; client, one string
   *    QUIT     server  &rarr; client, one string
   *    POST      server  &rarr; client, no arguments
   *    DONE   client  &rarr; server, one string
   *    GETSCORES   client  &rarr; server, arraylist of scores
   * @param cmd an integer corresponding to a command
   * @return String the textual representation of the command cmd
   */ 
   default String cmdToString(int cmd)
   {
      String cmdString;
      
      switch (cmd)
      {
         case START:
            cmdString = "START";
            break;
         case STOP: 
            cmdString = "STOP";
            break;
        case QUIT: 
            cmdString = "QUIT";
            break;
        case POST: 
            cmdString = "POST";
            break;
        case DONE: 
            cmdString = "DONE";
            break;
        case GETSCORES: 
            cmdString = "GETSCORES";
            break;
        case NAME: 
            cmdString = "NAME";
            break;
         default:
            cmdString = "UNRECOGNIZABLE COMMAND";
            System.out.println("COMMAND WAS "+cmd);
      }
      return cmdString;
   }
}
