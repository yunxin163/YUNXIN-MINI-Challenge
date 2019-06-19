#include <MsTimer2.h>

unsigned long MsTimer2::msecs;
void (*MsTimer2::func)();
volatile unsigned long MsTimer2::count;
volatile char MsTimer2::overflowing;
volatile unsigned int MsTimer2::tcnt2;
#if defined(__arm__) && defined(TEENSYDUINO)
static IntervalTimer itimer;
#endif

void MsTimer2::set(unsigned long ms, void (*f)()) {
	float prescaler = 0.0;
	
	if (ms == 0)
		msecs = 1;
	else
		msecs = ms;
		
	func = f;

#if defined (__AVR_ATmega168__) || defined (__AVR_ATmega48__) || defined (__AVR_ATmega88__) || defined (__AVR_ATmega328P__) || defined(__AVR_ATmega1280__) || defined(__AVR_ATmega2560__) || defined(__AVR_AT90USB646__) || defined(__AVR_AT90USB1286__)
	TIMSK2 &= ~(1<<TOIE2);
	TCCR2A &= ~((1<<WGM21) | (1<<WGM20));
	TCCR2B &= ~(1<<WGM22);
	ASSR &= ~(1<<AS2);
	TIMSK2 &= ~(1<<OCIE2A);
	
	if ((F_CPU >= 1000000UL) && (F_CPU <= 16000000UL)) {	
		TCCR2B |= (1<<CS22);
		TCCR2B &= ~((1<<CS21) | (1<<CS20));
		prescaler = 64.0;
	} else if (F_CPU < 1000000UL) {	
		TCCR2B |= (1<<CS21);
		TCCR2B &= ~((1<<CS22) | (1<<CS20));
		prescaler = 8.0;
	} else { 
		TCCR2B |= ((1<<CS22) | (1<<CS20));
		TCCR2B &= ~(1<<CS21);
		prescaler = 128.0;
	}
#elif defined (__AVR_ATmega8__)
	TIMSK &= ~(1<<TOIE2);
	TCCR2 &= ~((1<<WGM21) | (1<<WGM20));
	TIMSK &= ~(1<<OCIE2);
	ASSR &= ~(1<<AS2);
	
	if ((F_CPU >= 1000000UL) && (F_CPU <= 16000000UL)) {	
		TCCR2 |= (1<<CS22);
		TCCR2 &= ~((1<<CS21) | (1<<CS20));
		prescaler = 64.0;
	} else if (F_CPU < 1000000UL) {	
		TCCR2 |= (1<<CS21);
		TCCR2 &= ~((1<<CS22) | (1<<CS20));
		prescaler = 8.0;
	} else { 
		TCCR2 |= ((1<<CS22) && (1<<CS20));
		TCCR2 &= ~(1<<CS21);
		prescaler = 128.0;
	}
#elif defined (__AVR_ATmega128__)
	TIMSK &= ~(1<<TOIE2);
	TCCR2 &= ~((1<<WGM21) | (1<<WGM20));
	TIMSK &= ~(1<<OCIE2);
	
	if ((F_CPU >= 1000000UL) && (F_CPU <= 16000000UL)) {	
		TCCR2 |= ((1<<CS21) | (1<<CS20));
		TCCR2 &= ~(1<<CS22);
		prescaler = 64.0;
	} else if (F_CPU < 1000000UL) {	
		TCCR2 |= (1<<CS21);
		TCCR2 &= ~((1<<CS22) | (1<<CS20));
		prescaler = 8.0;
	} else { 
		TCCR2 |= (1<<CS22);
		TCCR2 &= ~((1<<CS21) | (1<<CS20));
		prescaler = 256.0;
	}
#elif defined (__AVR_ATmega32U4__)
	TCCR4B = 0;
	TCCR4A = 0;
	TCCR4C = 0;
	TCCR4D = 0;
	TCCR4E = 0;
	if (F_CPU >= 16000000L) {
		TCCR4B = (1<<CS43) | (1<<PSR4);
		prescaler = 128.0;
	} else if (F_CPU >= 8000000L) {
		TCCR4B = (1<<CS42) | (1<<CS41) | (1<<CS40) | (1<<PSR4);
		prescaler = 64.0;
	} else if (F_CPU >= 4000000L) {
		TCCR4B = (1<<CS42) | (1<<CS41) | (1<<PSR4);
		prescaler = 32.0;
	} else if (F_CPU >= 2000000L) {
		TCCR4B = (1<<CS42) | (1<<CS40) | (1<<PSR4);
		prescaler = 16.0;
	} else if (F_CPU >= 1000000L) {
		TCCR4B = (1<<CS42) | (1<<PSR4);
		prescaler = 8.0;
	} else if (F_CPU >= 500000L) {
		TCCR4B = (1<<CS41) | (1<<CS40) | (1<<PSR4);
		prescaler = 4.0;
	} else {
		TCCR4B = (1<<CS41) | (1<<PSR4);
		prescaler = 2.0;
	}
	tcnt2 = (int)((float)F_CPU * 0.001 / prescaler) - 1;
	OCR4C = tcnt2;
	return;
#elif defined(__arm__) && defined(TEENSYDUINO)
#else
#error Unsupported CPU type
#endif

	tcnt2 = 256 - (int)((float)F_CPU * 0.001 / prescaler);
}

void MsTimer2::start() {
	count = 0;
	overflowing = 0;
#if defined (__AVR_ATmega168__) || defined (__AVR_ATmega48__) || defined (__AVR_ATmega88__) || defined (__AVR_ATmega328P__) || defined (__AVR_ATmega1280__) || defined(__AVR_ATmega2560__) || defined(__AVR_AT90USB646__) || defined(__AVR_AT90USB1286__)
	TCNT2 = tcnt2;
	TIMSK2 |= (1<<TOIE2);
#elif defined (__AVR_ATmega128__)
	TCNT2 = tcnt2;
	TIMSK |= (1<<TOIE2);
#elif defined (__AVR_ATmega8__)
	TCNT2 = tcnt2;
	TIMSK |= (1<<TOIE2);
#elif defined (__AVR_ATmega32U4__)
	TIFR4 = (1<<TOV4);
	TCNT4 = 0;
	TIMSK4 = (1<<TOIE4);
#elif defined(__arm__) && defined(TEENSYDUINO)
	itimer.begin(MsTimer2::_overflow, 1000);
#endif
}

void MsTimer2::stop() {
#if defined (__AVR_ATmega168__) || defined (__AVR_ATmega48__) || defined (__AVR_ATmega88__) || defined (__AVR_ATmega328P__) || defined (__AVR_ATmega1280__) || defined(__AVR_ATmega2560__) || defined(__AVR_AT90USB646__) || defined(__AVR_AT90USB1286__)
	TIMSK2 &= ~(1<<TOIE2);
#elif defined (__AVR_ATmega128__)
	TIMSK &= ~(1<<TOIE2);
#elif defined (__AVR_ATmega8__)
	TIMSK &= ~(1<<TOIE2);
#elif defined (__AVR_ATmega32U4__)
	TIMSK4 = 0;
#elif defined(__arm__) && defined(TEENSYDUINO)
	itimer.end();
#endif
}

void MsTimer2::_overflow() {
	count += 1;
	
	if (count >= msecs && !overflowing) {
		overflowing = 1;
		count = count - msecs; 
		(*func)();
		overflowing = 0;
	}
}

#if defined (__AVR__)
#if defined (__AVR_ATmega32U4__)
ISR(TIMER4_OVF_vect) {
#else
ISR(TIMER2_OVF_vect) {
#endif
#if defined (__AVR_ATmega168__) || defined (__AVR_ATmega48__) || defined (__AVR_ATmega88__) || defined (__AVR_ATmega328P__) || defined (__AVR_ATmega1280__) || defined(__AVR_ATmega2560__) || defined(__AVR_AT90USB646__) || defined(__AVR_AT90USB1286__)
	TCNT2 = MsTimer2::tcnt2;
#elif defined (__AVR_ATmega128__)
	TCNT2 = MsTimer2::tcnt2;
#elif defined (__AVR_ATmega8__)
	TCNT2 = MsTimer2::tcnt2;
#elif defined (__AVR_ATmega32U4__)
#endif
	MsTimer2::_overflow();
}
#endif 

