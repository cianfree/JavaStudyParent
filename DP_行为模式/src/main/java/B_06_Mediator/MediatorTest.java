package B_06_Mediator;

interface IMediator {
    void fight();

    void talk();

    void registerA(ColleagueA a);

    void registerB(ColleagueB a);
}

//concrete mediator
class ConcreteMediator implements IMediator {

    ColleagueA talk;
    ColleagueB fight;

    public void registerA(ColleagueA a) {
        talk = a;
    }

    public void registerB(ColleagueB b) {
        fight = b;
    }

    public void fight() {
        System.out.println("Mediator is fighting");//let the fight colleague do some stuff
    }

    public void talk() {
        System.out.println("Mediator is talking");//let the talk colleague do some stuff
    }
}

abstract class BaseColleague {
    IMediator mediator;

    public abstract void doSomething();
}

//concrete colleague
class ColleagueA extends BaseColleague {

    public ColleagueA(IMediator mediator) {
        this.mediator = mediator;
        this.mediator.registerA(this);
    }

    @Override
    public void doSomething() {
        this.mediator.talk();
    }
}

//concrete colleague
class ColleagueB extends BaseColleague {

    public ColleagueB(IMediator mediator) {
        this.mediator = mediator;
        this.mediator.registerB(this);
    }

    @Override
    public void doSomething() {
        this.mediator.fight();
    }
}

public class MediatorTest {
    public static void main(String[] args) {
        IMediator mediator = new ConcreteMediator();

        ColleagueA talkColleague = new ColleagueA(mediator);
        ColleagueB fightColleague = new ColleagueB(mediator);

        talkColleague.doSomething();
        fightColleague.doSomething();
    }
}