  0         JUMP         L15
  1  L10:   LOADL        1
  2         LOAD         -1[ST]
  3         JUMPIF (0)   L11
  4         LOADL        1
  5         CALL         not     
  6         CALL         and     
  7  L11:   LOAD         -1[ST]
  8         JUMPIF (1)   L12
  9         LOADL        1
 10         LOADL        0
 11         CALL         not     
 12         CALL         eq      
 13         CALL         or      
 14  L12:   JUMPIF (0)   L13
 15         LOADL        20
 16         CALL         putint  
 17         JUMP         L14
 18  L13:   LOADL        1
 19         CALL         neg     
 20         CALL         putint  
 21  L14:   RETURN (0)   0
 22  L15:   LOADL        -1
 23         LOADL        1
 24         LOADA        1[CB]
 25         LOADL        -1
 26         CALL         L10
 27         HALT   (0)   
