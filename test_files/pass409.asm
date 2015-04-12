  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        4
  3         STORE        3[LB]
  4         PUSH         1
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         STORE        4[LB]
  8         PUSH         1
  9         LOADL        1
 10         STORE        5[LB]
 11         LOAD         4[LB]
 12         LOADL        0
 13         LOAD         5[LB]
 14         CALL         arrayupd
 15         JUMP         L12
 16  L11:   LOAD         4[LB]
 17         LOAD         5[LB]
 18         LOAD         4[LB]
 19         LOAD         5[LB]
 20         LOADL        1
 21         CALL         sub     
 22         CALL         arrayref
 23         LOAD         5[LB]
 24         CALL         add     
 25         CALL         arrayupd
 26         LOAD         5[LB]
 27         LOADL        1
 28         CALL         add     
 29         STORE        5[LB]
 30  L12:   LOAD         5[LB]
 31         LOAD         3[LB]
 32         CALL         lt      
 33         JUMPIF (1)   L11
 34         PUSH         1
 35         LOAD         4[LB]
 36         LOADL        3
 37         CALL         arrayref
 38         LOADL        2
 39         CALL         add     
 40         STORE        6[LB]
 41         LOAD         6[LB]
 42         CALL         putint  
 43         RETURN (0)   0
 44  L13:   LOADL        -1
 45         LOADL        1
 46         LOADA        1[CB]
 47         LOADL        -1
 48         CALL         L10
 49         HALT   (0)   
