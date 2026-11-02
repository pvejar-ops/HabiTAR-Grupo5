import unittest
from fastapi.testclient import TestClient
from app.main import app
from app.services.scoring.catq import calculate_catq_score
from app.services.scoring.rbq2a import calculate_rbq2a_score


class TestScoringCalculations(unittest.TestCase):
    def test_catq_neutral_scores(self):
        """Si todos los ítems son 4, los inversos (8-4=4) también dan 4 -> Total 100."""
        answers = {i: 4 for i in range(1, 26)}
        result = calculate_catq_score(answers)
        self.assertEqual(result.total_score, 100)
        self.assertEqual(result.compensation_score, 36)
        self.assertEqual(result.masking_score, 32)
        self.assertEqual(result.assimilation_score, 32)

    def test_catq_inversion_logic(self):
        """Verifica que los 5 ítems invertidos (3, 12, 19, 22, 24) se calculen como 8 - valor."""
        # Todos en 1, excepto los inversos que dan 8-1=7
        answers = {i: 1 for i in range(1, 26)}
        result = calculate_catq_score(answers)
        # 20 directos * 1 = 20. 5 inversos * 7 = 35. Total = 55.
        self.assertEqual(result.total_score, 55)

        # Todos en 7, excepto los inversos que dan 8-7=1
        answers_7 = {i: 7 for i in range(1, 26)}
        result_7 = calculate_catq_score(answers_7)
        # 20 directos * 7 = 140. 5 inversos * 1 = 5. Total = 145.
        self.assertEqual(result_7.total_score, 145)

    def test_catq_validation_errors(self):
        # Faltan ítems
        with self.assertRaises(ValueError):
            calculate_catq_score({1: 4, 2: 4})

        # Valor fuera de rango (8)
        invalid_val = {i: 4 for i in range(1, 26)}
        invalid_val[5] = 8
        with self.assertRaises(ValueError):
            calculate_catq_score(invalid_val)

        # Valor fuera de rango (0)
        invalid_val[5] = 0
        with self.assertRaises(ValueError):
            calculate_catq_score(invalid_val)

    def test_rbq2a_means(self):
        # Todos en 1
        answers_1 = {i: 1 for i in range(1, 21)}
        res_1 = calculate_rbq2a_score(answers_1)
        self.assertEqual(res_1.total_mean, 1.00)
        self.assertEqual(res_1.repetitive_motor_mean, 1.00)
        self.assertEqual(res_1.insistence_on_sameness_mean, 1.00)

        # Todos en 3
        answers_3 = {i: 3 for i in range(1, 21)}
        res_3 = calculate_rbq2a_score(answers_3)
        self.assertEqual(res_3.total_mean, 3.00)
        self.assertEqual(res_3.repetitive_motor_mean, 3.00)
        self.assertEqual(res_3.insistence_on_sameness_mean, 3.00)

    def test_rbq2a_normalization(self):
        """Verifica que el valor 4 se normalice a 3 según Barrett et al. (2015)."""
        answers = {i: 4 for i in range(1, 21)}
        res = calculate_rbq2a_score(answers)
        self.assertEqual(res.total_mean, 3.00)

    def test_rbq2a_validation_errors(self):
        # Faltan ítems
        with self.assertRaises(ValueError):
            calculate_rbq2a_score({1: 2})

        # Valor inválido (5 o 0)
        answers = {i: 2 for i in range(1, 21)}
        answers[3] = 5
        with self.assertRaises(ValueError):
            calculate_rbq2a_score(answers)


class TestScreeningEndpoint(unittest.TestCase):
    def setUp(self):
        self.client = TestClient(app)

    def test_endpoint_process_success(self):
        catq_answers = {i: 4 for i in range(1, 26)}
        rbq_answers = {i: 2 for i in range(1, 21)}

        payload = {
            "user_id": "test-user-001",
            "catq": {"answers": catq_answers},
            "rbq2a": {"answers": rbq_answers},
        }

        response = self.client.post("/api/v1/screening/process", json=payload)
        self.assertEqual(response.status_code, 200)
        data = response.json()
        self.assertEqual(data["user_id"], "test-user-001")
        self.assertIsNotNone(data["catq_result"])
        self.assertEqual(data["catq_result"]["total_score"], 100)
        self.assertIsNotNone(data["rbq2a_result"])
        self.assertEqual(data["rbq2a_result"]["total_mean"], 2.00)
        self.assertIn("Aviso Importante", data["disclaimer"])

    def test_endpoint_process_validation_error(self):
        payload = {
            "user_id": "test-user-002",
            "catq": {"answers": {1: 5}},  # Incompleto (faltan 24 ítems)
        }
        response = self.client.post("/api/v1/screening/process", json=payload)
        self.assertEqual(response.status_code, 400)
        data = response.json()
        self.assertIn("Faltan ítems", data["detail"])


if __name__ == "__main__":
    unittest.main()
