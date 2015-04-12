  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        4
  6         STORE        4[LB]
  7         PUSH         1
  8         LOAD         4[LB]
  9         CALL         newarr  
 10         STORE        5[LB]
 11         PUSH         1
 12         LOADL        0
 13         STORE        6[LB]
 14         LOADL        1
 15         STORE        6[LB]
 16         LOAD         5[LB]
 17         LOADL        0
 18         LOAD         6[LB]
 19         CALL         arrayupd
 20         JUMP         L12
 21  L11:   LOAD         5[LB]
 22         LOAD         6[LB]
 23         LOAD         5[LB]
 24         LOAD         6[LB]
 25         LOADL        1
 26         CALL         sub     
 27         CALL         arrayref
 28         LOAD         6[LB]
 29         CALL         add     
 30         CALL         arrayupd
 31         LOAD         6[LB]
 32         LOADL        1
 33         CALL         add     
 34         STORE        6[LB]
 35  L12:   LOAD         6[LB]
 36         LOAD         4[LB]
 37         CALL         lt      
 38         JUMPIF (1)   L11
 39         LOAD         5[LB]
 40         LOADL        3
 41         CALL         arrayref
 42         LOADL        2
 43         CALL         add     
 44         STORE        3[LB]
 45         LOAD         3[LB]
 46         CALL         putint  
 47         RETURN (0)   0
 48  L13:   LOADL        -1
 49         LOADL        1
 50         LOADA        1[CB]
 51         LOADL        -1
 52         CALL         L10
 53         HALT   (0)   
