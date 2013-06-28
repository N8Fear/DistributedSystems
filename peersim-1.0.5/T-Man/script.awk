BEGIN {
	LARGE = 10^10;
}
function initialize ( nr ) {
	min [ nr ] = LARGE ;
	max [ nr ] = -1;
	avg [ nr ] = 0;
	navg [ nr ] = 0;
	initialized [ kNr ] = 1;
}
" control . ring " {
	kNr = $5 ;
	if ( $6 == " ENDTIME " && $9 == " NEXT ")
	{
		if ( initialized [ kNr ] == 0)
			initialize ( kNr ) ;
		time = $7 ;
		avg [ kNr ] += time ;
		navg [ kNr ]++;
		if ( min [ kNr ] > time )
			min [ kNr ] = time ;
		if ( max [ kNr ] < time )
			max [ kNr ] = time ;
	}
}
==0)
END {
	for ( i in initialized )
		printf ("% d % f % f % f \ n " , i , avg [ i ]/ navg [ i ] ,
}

