package cn.withzz.game;

/**
 * 用于生成性格迥异的AI~
 * 生成初试属性不同的AI对象
 */
public class AIFactory {
	public static Man getAI(int type, int x, int y, Panel p) {
		Man m = new Man(x, y, 4, p, null);
		switch (type) {
		case 1:// hunter 移动快速，但泡泡威力和数量少
			m.setSpeed(4);
			m.setMax(4);
			m.setName("hunter");
			break;
		case 2:// warrior 移动极慢 但是泡泡威力较大
			m.setSpeed(9);
			m.setPower(3);
			m.setMax(1);
			m.setName("warrior");
			break;
		case 3:// oracle  泡泡数量较多
			m.setSpeed(8);
			m.setPower(2);
			m.setMax(4);
			m.setName("oracle");
			break;
		case 4:// zz    最强AI 属性全满
			m.setSpeed(4);
			m.setPower(10);
			m.setMax(10);
			m.setName("zz");
			break;

		}
		return m;
	}
}
