  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        2
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        2
  6         LOADL        5
  7         CALL         mult    
  8         LOADL        3
  9         CALL         sub     
 10         CALL         newarr  
 11         STORE        4[LB]
 12         LOAD         4[LB]
 13         LOADL        0
 14         LOADL        13
 15         CALL         arrayupd
 16         PUSH         1
 17         LOAD         4[LB]
 18         LOAD         3[LB]
 19         LOADL        2
 20         CALL         sub     
 21         CALL         arrayref
 22         LOAD         3[LB]
 23         CALL         gt      
 24         STORE        5[LB]
 25         LOAD         5[LB]
 26         JUMPIF (0)   L11
 27         LOAD         4[LB]
 28         LOADL        0
 29         CALL         arrayref
 30         CALL         putint  
 31         JUMP         L12
 32  L11:   LOADL        1
 33         CALL         neg     
 34         CALL         putint  
 35  L12:   RETURN (0)   0
 36  L13:   LOADL        -1
 37         LOADL        1
 38         LOADA        1[CB]
 39         LOADL        -1
 40         CALL         L10
 41         HALT   (0)   
