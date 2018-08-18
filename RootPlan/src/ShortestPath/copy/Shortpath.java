package ShortestPath.copy;

//대중교통 관련 클래스
public class Shortpath {
   int min;
   int visit[];
   int listSize;
   int tmp[]; // 임시 경로 순서
   public static int carAns[];
   public static int ptAns[];

   public void init(int size){
      min=Integer.MAX_VALUE;
      listSize = size;
      visit = new int[10];
      tmp = new int[10];
      carAns = new int[10];
      ptAns = new int[10];
   }
   
   void dfs(int cnt, int now, int sum, int end, int how) { // how : 0 대중교통, 1 자동차
      if(sum > min) return;
      if(cnt == listSize-1) {
         if(how==0)
            sum +=  Route.ptDist[now][end].getTime();
         else
            sum += Route.carDist[now][end].getTime();
         
         if(min>sum) {
            for(int i=0; i<listSize - 1; i++) {
               if(how==0)    ptAns[i] = tmp[i];
               else  carAns[i] = tmp[i];
            }
            System.out.println("cnt"+cnt+", end: "+ end);
            if(how == 0)
               ptAns[cnt] = end;
            else 
               carAns[cnt] = end;
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1 || i == now || i == end) continue;      
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { // 대중교통
            dfs(cnt+1, i, sum+Route.ptDist[now][i].getTime(), end, 0);   
         }else { // 자동차
            dfs(cnt+1, i, sum+Route.carDist[now][i].getTime(), end, 1);
         }
         tmp[cnt]=-1;
         visit[i] = 0;
      }
   }
   
   void dfsEqual(int cnt, int now, int sum, int start, int how) { // how : 0 대중교통, 1 자동차
      if(sum > min) return;
      if(cnt == listSize) {
         // 시작점까지 가는 거리 더해주기
         if(how==0)
            sum +=  Route.ptDist[now][start].getTime();
         else
            sum += Route.carDist[now][start].getTime();
         
         if(min>sum) {
            for(int i=0; i<listSize; i++) {
               if(how==0)    ptAns[i] = tmp[i];
               else  carAns[i] = tmp[i];
            }
            if(how==0)    ptAns[listSize] = start;
            else  carAns[listSize] = start;
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1|| i == now) continue;
         
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { // 대중교통
            dfsEqual(cnt+1, i, sum+Route.ptDist[now][i].getTime(), start, 0);   
         }else { // 자동차
            dfsEqual(cnt+1, i, sum+Route.carDist[now][i].getTime(), start, 1);
         }
         tmp[cnt]=-1;
         visit[i] = 0;
      }
   }
   static void carPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(Route.carDist[i][j].getTime() + " ");
         }
         System.out.println();
      }
   }
   
   static void ptPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(Route.ptDist[i][j].getTime() + " ");
         }
         System.out.println();
      }
   }
   
   void callDFS(int start, int end, int how, int equal){ // equal: 시작, 도착 같으면 1  아니면 0
      System.out.println("자동차 거리 출력");
      carPrint(listSize);
      System.out.println("대중교통 거리 출력");
      ptPrint(listSize);
      System.out.println(start+" "+end);
      if(equal==0) {
         // 시작점 바꾸기
            visit[start] = 1;
            tmp[0] = start;
            if(how==0) { // 대중교통
               dfs(1, start, 0, end, 0);   
            }else { // 자동차
               dfs(1, start, 0, end, 1);
            }
            tmp[0]=-1;
            visit[start]= 0;
      }else {
         // 시작점 바꾸기
         visit[start] = 1;
         tmp[0] = start;
         if(how==0) { // 대중교통
            dfsEqual(1, start, 0, start, 0);   
         }else { // 자동차
            dfsEqual(1, start, 0, start, 1);
         }
         tmp[0]=-1;
         visit[start]= 0;
      }
      
      System.out.println("대중교통 순서:");
      for(int i =0;i<listSize;i++) {
         System.out.print(ptAns[i] +" ");
      }
      System.out.println();

      System.out.println("자동차 순서:");
      for(int i =0;i<listSize;i++) {
          System.out.print(carAns[i] +" ");
       }
       System.out.println();
      
   }
}