  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        2
  3         CALL         newarr  
  4         STORE        3[LB]
  5         PUSH         1
  6         LOADA        3[SB]
  7         LOADL        1
  8         CALL         newobj  
  9         STORE        4[LB]
 10         LOAD         4[LB]
 11         LOADL        0
 12         LOADL        3
 13         CALL         fieldupd
 14         LOAD         3[LB]
 15         LOADL        0
 16         LOAD         4[LB]
 17         CALL         arrayupd
 18         PUSH         1
 19         LOADA        3[SB]
 20         LOADL        1
 21         CALL         newobj  
 22         STORE        5[LB]
 23         LOAD         5[LB]
 24         LOADL        0
 25         LOADL        5
 26         CALL         fieldupd
 27         LOAD         3[LB]
 28         LOADL        1
 29         LOAD         5[LB]
 30         CALL         arrayupd
 31         PUSH         1
 32         LOAD         3[LB]
 33         LOADL        0
 34         CALL         arrayref
 35         STORE        6[LB]
 36         PUSH         1
 37         LOAD         3[LB]
 38         LOADL        1
 39         CALL         arrayref
 40         STORE        7[LB]
 41         PUSH         1
 42         LOAD         6[LB]
 43         LOADL        0
 44         CALL         fieldref
 45         LOAD         7[LB]
 46         LOADL        0
 47         CALL         fieldref
 48         CALL         add     
 49         LOADL        13
 50         CALL         add     
 51         STORE        8[LB]
 52         LOAD         8[LB]
 53         CALL         putint  
 54         RETURN (0)   0
 55  L11:   LOADL        -1
 56         LOADL        1
 57         LOADA        1[CB]
 58         JUMP         L12
 59  L12:   LOADL        -1
 60         LOADL        0
 61         LOADL        -1
 62         CALL         L10
 63         HALT   (0)   
