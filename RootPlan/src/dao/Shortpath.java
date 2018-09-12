package dao;

import dto.DataTotal;

//대중교통 관련 클래스
public class Shortpath {
   int min;
   int visit[];
   int listSize;
   int tmp[]; // 임시 경로 순서
<<<<<<< HEAD
   public DataTotal dataTotal;

   public Shortpath() {}
   public void init(int size, DataTotal dataTotal){
=======
   public int carAns[];
   public int ptAns[];
   int id = 0;
   public void init(int size, int id){
>>>>>>> refs/heads/branch4
      min=Integer.MAX_VALUE;
      listSize = size;
      visit = new int[size];
      tmp = new int[size];
<<<<<<< HEAD
      this.dataTotal = dataTotal;
=======
      carAns = new int[size];
      ptAns = new int[size];
      this.id = id;
>>>>>>> refs/heads/branch4
   }
   
   void dfs(int cnt, int now, int sum, int end, int how) { // how : 0 대중교통, 1 자동차
      if(sum > min) return;
      if(cnt == listSize-1) {
         if(how==0)
<<<<<<< HEAD
            sum +=  dataTotal.ptDist[now][end].getTime();
=======
            sum +=  Route.ptDist[id][now][end].getTime();
>>>>>>> refs/heads/branch4
         else
<<<<<<< HEAD
            sum += dataTotal.carDist[now][end].getTime();
=======
            sum += Route.carDist[id][now][end].getTime();
>>>>>>> refs/heads/branch4
         
         if(min>sum) {
            for(int i=0; i<listSize - 1; i++) {
               if(how==0)    dataTotal.ptAns[i] = tmp[i];
               else  dataTotal.carAns[i] = tmp[i];
            }
            System.out.println("cnt"+cnt+", end: "+ end);
            if(how == 0)
            	dataTotal.ptAns[cnt] = end;
            else 
            	dataTotal.carAns[cnt] = end;
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1 || i == now || i == end) continue;      
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { // 대중교통
<<<<<<< HEAD
            dfs(cnt+1, i, sum+dataTotal.ptDist[now][i].getTime(), end, 0);   
=======
            dfs(cnt+1, i, sum+Route.ptDist[id][now][i].getTime(), end, 0);   
>>>>>>> refs/heads/branch4
         }else { // 자동차
<<<<<<< HEAD
            dfs(cnt+1, i, sum+dataTotal.carDist[now][i].getTime(), end, 1);
=======
            dfs(cnt+1, i, sum+Route.carDist[id][now][i].getTime(), end, 1);
>>>>>>> refs/heads/branch4
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
<<<<<<< HEAD
            sum +=  dataTotal.ptDist[now][start].getTime();
=======
            sum +=  Route.ptDist[id][now][start].getTime();
>>>>>>> refs/heads/branch4
         else
<<<<<<< HEAD
            sum += dataTotal.carDist[now][start].getTime();
=======
            sum += Route.carDist[id][now][start].getTime();
>>>>>>> refs/heads/branch4
         
         if(min>sum) {
            for(int i=0; i<listSize; i++) {
               if(how==0)    dataTotal.ptAns[i] = tmp[i];
               else  dataTotal.carAns[i] = tmp[i];
            }
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1|| i == now) continue;
         
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { // 대중교통
<<<<<<< HEAD
            dfsEqual(cnt+1, i, sum+dataTotal.ptDist[now][i].getTime(), start, 0);   
=======
            dfsEqual(cnt+1, i, sum+Route.ptDist[id][now][i].getTime(), start, 0);   
>>>>>>> refs/heads/branch4
         }else { // 자동차
<<<<<<< HEAD
            dfsEqual(cnt+1, i, sum+dataTotal.carDist[now][i].getTime(), start, 1);
=======
            dfsEqual(cnt+1, i, sum+Route.carDist[id][now][i].getTime(), start, 1);
>>>>>>> refs/heads/branch4
         }
         tmp[cnt]=-1;
         visit[i] = 0;
      }
   }
   void carPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
<<<<<<< HEAD
            System.out.print(dataTotal.carDist[i][j].getTime() + " ");
=======
            System.out.print(Route.carDist[id][i][j].getTime() + " ");
>>>>>>> refs/heads/branch4
         }
         System.out.println();
      }
   }
   
   void ptPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
<<<<<<< HEAD
            System.out.print(dataTotal.ptDist[i][j].getTime() + " ");
=======
            System.out.print(Route.ptDist[id][i][j].getTime() + " ");
>>>>>>> refs/heads/branch4
         }
         System.out.println();
      }
   }
   
   void callDFS(int start, int end, int how, int equal){ // equal: 시작, 도착 같으면 1  아니면 0
      System.out.println("자동차 거리 출력"+start);
      //System.out.println("listSize 출력 : " + listSize);
      carPrint(listSize);
      System.out.println("대중교통 거리 출력");
      ptPrint(listSize);
      min=Integer.MAX_VALUE;
      System.out.println("start : " + start);
      System.out.println("end : " + end);
      
      if(start!=end) {
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
      }else { // 사이클 생성 
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
      if(how == 0) {
	      System.out.println("대중교통 순서:");
	      for(int i =0;i<listSize;i++) {
	         System.out.print(dataTotal.ptAns[i] +" ");
	      }
      }else {
	      System.out.println("자동차 순서:");
	      for(int i =0;i<listSize;i++) {
	         System.out.print(dataTotal.carAns[i] +" ");
	      }
      }
      System.out.println();
   }
}