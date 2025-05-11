package com.example.caculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private TextView txtInput, txtResult;
    private String currentInput = "";
    private List<String> expression = new ArrayList<>(); // Lưu biểu thức toán học


    private  ImageButton ibtn_return_home ;
//    Button btn_0;
//    Button btn_equal;
//    TextView txt_input;
//    TextView txt_result;

    //----
    private LinearLayout ll_number;
    private LinearLayout ll_euation;
//    private LinearLayout ll_convert;
    boolean  check_equation = false;
    boolean check_convert = false;
    private Button btn_euqation;
    private Button btn_convert;
    //----
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        txtInput = findViewById(R.id.txt_input);


        txtResult = findViewById(R.id.txt_result);
        mapping();
        // func thay doi layout Equation, Convert
        Change_Equation();
//        Change_Convert();
        // Gán sự kiện cho các nút
        setNumberClickListeners();
        setOperatorClickListeners();
        setUtilityClickListeners();
    }

    //--- ham anh xa -------
    public  void mapping(){
        ll_number = findViewById(R.id.layout_number);
        ll_euation = findViewById(R.id.layout_equation);
//        ll_convert = findViewById(R.id.layout_convert);

        btn_euqation = findViewById(R.id.btn_equation);

//        btn_convert = findViewById(R.id.btn_convert);

        ibtn_return_home = findViewById(R.id.btn_return_home);
//        //-------
//
//        btn_0 = (Button)  findViewById(R.id.btn_0);
//        btn_equal = (Button) findViewById(R.id.btn_equal);
//        txt_input = (TextView) findViewById(R.id.txt_input);
//        txt_result = (TextView) findViewById(R.id.txt_result);
    }

    private boolean isResultDisplayed = false; // Biến kiểm tra nếu đang hiển thị kết quả

    private void setNumberClickListeners() {
        int[] numberIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_dot,
                R.id.btn_0_fun,R.id.btn_1_fun,R.id.btn_2_fun,
                R.id.btn_3_fun,R.id.btn_4_fun,R.id.btn_5_fun,
                R.id.btn_6_fun,R.id.btn_7_fun,R.id.btn_8_fun,
                R.id.btn_9_fun,R.id.btn_9_fun,R.id.btn_dot_fun
        };

        int[] parenthesisIds = {
                R.id.btn_open_left, R.id.btn_close_right,
                R.id.btn_open_left_fun,R.id.btn_close_right_fun
        };
        // Lắng nghe số và dấu "."
        View.OnClickListener numberListener = view -> {
            if (isResultDisplayed) {
                isResultDisplayed = false; // Chuyển về trạng thái nhập liệu
                expression.clear(); // Xóa biểu thức cũ
                txtResult.setText("Result");
                txtInput.setText(""); // Xóa hiển thị biểu thức cũ
            }
            Button button = (Button) view;
            currentInput += button.getText().toString();
            txtInput.setText(txtInput.getText().toString() + button.getText().toString());
        };
        // Lắng nghe ngoặc
        View.OnClickListener parenthesisListener = view -> {
            Button button = (Button) view;
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            expression.add(button.getText().toString());
            txtInput.setText(txtInput.getText().toString() + button.getText().toString());
        };

        // Gán sự kiện cho số
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }
        // Gán sự kiện cho ngoặc
        for (int id : parenthesisIds) {
            findViewById(id).setOnClickListener(parenthesisListener);
        }
    }

    private void setOperatorClickListeners() {
        int[] operatorIds = {
                R.id.btn_plus, R.id.btn_minus,
                R.id.btn_multi, R.id.btn_divide,
                R.id.btn_plus_fun,R.id.btn_minus_fun,
                R.id.btn_multi_fun,R.id.btn_divide_fun
        };

        View.OnClickListener listener = view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            Button button = (Button) view;
            expression.add(button.getText().toString());
            txtInput.setText(txtInput.getText().toString() + " " + button.getText().toString() + " ");
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setUtilityClickListeners() {
        findViewById(R.id.btn_equal).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            try {
                double result = evaluateExpression(expression);
                txtResult.setText(String.valueOf(result));
                isResultDisplayed = true; // Đánh dấu đã hiển thị kết quả
            } catch (Exception e) {
                txtResult.setText("Error");
            }
        });
        findViewById(R.id.btn_equal_fun).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            try {
                double result = evaluateExpression(expression);
                txtResult.setText(String.valueOf(result));
                isResultDisplayed = true; // Đánh dấu đã hiển thị kết quả
            } catch (IllegalArgumentException e) {
                txtResult.setText("Error: " + e.getMessage());
            } catch (Exception e) {
                txtResult.setText("Error: Invalid Expression");
            }
        });



        findViewById(R.id.btn_CE).setOnClickListener(view -> {
            currentInput = "";
            expression.clear();
            txtInput.setText("");
            txtResult.setText("Result");
        });
        findViewById(R.id.btn_CE_fun).setOnClickListener(view -> {
            currentInput = "";
            expression.clear();
            txtInput.setText("");
            txtResult.setText("Result");
        });

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (isResultDisplayed) {
                // Nếu đang hiển thị kết quả, trở lại trạng thái chỉnh sửa biểu thức cũ
                isResultDisplayed = false;
                //txtResult.setText("Result");
                txtInput.setText(formatExpression(expression)); // Hiển thị lại biểu thức
            } else {
                if (!currentInput.isEmpty()) {
                    // Xóa ký tự cuối cùng trong currentInput
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                } else if (!expression.isEmpty()) {
                    // Nếu currentInput rỗng, xóa phần tử cuối cùng trong biểu thức
                    expression.remove(expression.size() - 1);
                }
                // Cập nhật lại giao diện ngay lập tức
                txtInput.setText(formatExpression(expression) + currentInput);
            }
        });

        findViewById(R.id.btn_delete_fun).setOnClickListener(view -> {
            if (isResultDisplayed) {
                // Nếu đang hiển thị kết quả, trở lại trạng thái chỉnh sửa biểu thức cũ
                isResultDisplayed = false;
                //txtResult.setText("Result");
                txtInput.setText(formatExpression(expression)); // Hiển thị lại biểu thức
            } else {
                if (!currentInput.isEmpty()) {
                    // Xóa ký tự cuối cùng trong currentInput
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                } else if (!expression.isEmpty()) {
                    // Nếu currentInput rỗng, xóa phần tử cuối cùng trong biểu thức
                    expression.remove(expression.size() - 1);
                }
                // Cập nhật lại giao diện ngay lập tức
                txtInput.setText(formatExpression(expression) + currentInput);
            }
        });

        findViewById(R.id.btn_pow2).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Thêm "²" vào currentInput nhưng không tính toán ngay
                currentInput += "²";
                txtInput.setText(txtInput.getText().toString() + "²");
            }
        });

        findViewById(R.id.btn_pow_ab).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Thêm cơ số vào biểu thức
                expression.add(currentInput);
                currentInput = "";
                // Thêm toán tử "^"
                expression.add("^");
                txtInput.setText(txtInput.getText().toString() + " ^ ");
            }
        });

        findViewById(R.id.btn_log_ab).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu có giá trị đang nhập, thêm vào biểu thức
                expression.add(currentInput);
                currentInput = "";
            }
            // Thêm toán tử log và yêu cầu nhập b
            expression.add("log");
            txtInput.setText(txtInput.getText().toString() + " log ");
        });

        findViewById(R.id.btn_ln).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu có giá trị đang nhập, thêm vào biểu thức
                expression.add(currentInput);
                currentInput = "";
            }
            expression.add("ln"); // Thêm "ln" vào biểu thức
            txtInput.setText(txtInput.getText().toString() + "ln("); // Hiển thị "ln("
        });

        findViewById(R.id.btn_can2).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu đã có đầu vào, thêm số đó vào biểu thức trước
                expression.add(currentInput);
                currentInput = "";
            }
            // Thêm toán tử √ và chờ số tiếp theo
            expression.add("sqrt");
            txtInput.setText(txtInput.getText().toString() + " √");
        });

        findViewById(R.id.btn_sin).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
// Dù `currentInput` rỗng, vẫn cho phép thêm toán tử
            expression.add("sin");

            txtInput.setText(txtInput.getText().toString() + " sin");
        });
        findViewById(R.id.btn_cot).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu đã có đầu vào, thêm số đó vào biểu thức trước
                expression.add(currentInput);
                currentInput = "";
            }
            // Dù currentInput rỗng, vẫn cho phép thêm toán tử cot
            expression.add("cot");
            txtInput.setText(txtInput.getText().toString() + " cot");
        });

        findViewById(R.id.btn_tan).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu đã có đầu vào, thêm số đó vào biểu thức trước
                expression.add(currentInput);
                currentInput = "";
            }
            // Dù currentInput rỗng, vẫn cho phép thêm toán tử tan
            expression.add("tan");
            txtInput.setText(txtInput.getText().toString() + " tan");
        });

        findViewById(R.id.btn_cos).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                // Nếu đã có đầu vào, thêm số đó vào biểu thức trước
                expression.add(currentInput);
                currentInput = "";
            }
            // Dù currentInput rỗng, vẫn cho phép thêm toán tử cos
            expression.add("cos");
            txtInput.setText(txtInput.getText().toString() + " cos");
        });
        findViewById(R.id.btn_percent).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            // Thêm phần trăm vào biểu thức
            expression.add("%");
            txtInput.setText(txtInput.getText().toString() + " %");
        });
        findViewById(R.id.btn_fact).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            // Thêm giai thừa vào biểu thức
            expression.add("!");
            txtInput.setText(txtInput.getText().toString() + " !");
        });



    }

    private String formatExpression(List<String> expression) {
        return String.join(" ", expression) + " ";
    }

    private double evaluateExpression(List<String> expression) {
        List<String> postfix = convertToPostfix(expression);
        return evaluatePostfix(postfix);
    }
    private List<String> convertToPostfix(List<String> expression) {
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : expression) {
            if (isNumeric(token)) {
                postfix.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            }
            else if (token.equals("sin") || token.equals("cos") || token.equals("tan") || token.equals("cot") || token.equals("sqrt")) {
                // Các toán tử đơn như sqrt, sin, cos, cot, tan thêm trực tiếp vào postfix
                postfix.add(token);
            }
            else if (token.equals("%") || token.equals("!")) {
                // Các toán tử phần trăm và giai thừa cũng được thêm vào postfix
                postfix.add(token);
            }
            else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    // Độ ưu tiên của các toán tử
    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
            case "log":
            case "ln":
            case "sqrt":
            case "cot":
            case "tan":
            case "cos": // Các hàm toán học đơn có độ ưu tiên cao
                return 3;
            default:
                return 0;
        }
    }




    private double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("+")) {
                stack.push(stack.pop() + stack.pop());
            } else if (token.equals("-")) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(a - b);
            } else if (token.equals("X")) {
                stack.push(stack.pop() * stack.pop());
            } else if (token.equals("/")) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(a / b);
            } else if (token.equals("^")) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(Math.pow(a, b));
            } else if (token.equals("log")) {
                double b = stack.pop();
                double a = stack.pop();
                if (a <= 0 || b <= 0 || a == 1) {
                    throw new IllegalArgumentException("Invalid input for log(a, b)");
                }
                stack.push(Math.log(b) / Math.log(a)); // Tính log(a, b)
            } else if (token.equals("ln")) {
                double a = stack.pop();
                if (a <= 0) {
                    throw new IllegalArgumentException("Invalid input for ln");
                }
                stack.push(Math.log(a)); // Tính ln(a)
            }
            else if (token.equals("sqrt")) {
                // Nếu thiếu số, tự động lấy 0 làm giá trị (hoặc báo lỗi nếu cần)
                double a = stack.isEmpty() ? 0 : stack.pop();
                if (a < 0) {
                    throw new IllegalArgumentException("Cannot calculate square root of a negative number");
                }
                stack.push(Math.sqrt(a)); // Tính căn bậc 2
            }
            else if (token.equals("sin")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                stack.push(Math.sin(Math.toRadians(a))); // Tính sin với giá trị theo độ
            } else if (token.equals("cos")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                stack.push(Math.cos(Math.toRadians(a))); // Tính cos với giá trị theo độ
            } else if (token.equals("tan")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                stack.push(Math.tan(Math.toRadians(a))); // Tính tan với giá trị theo độ
            } else if (token.equals("cot")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                if (a == 0) {
                    throw new IllegalArgumentException("Cannot calculate cotangent of 0");
                }
                stack.push(1 / Math.tan(Math.toRadians(a))); // Tính cot với giá trị theo độ
            }
            else if (token.equals("%")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                stack.push(a / 100); // Tính phần trăm
            } else if (token.equals("!")) {
                double a = stack.isEmpty() ? 0 : stack.pop();
                stack.push(factorial((int) a)); // Tính giai thừa
            }
        }

        return stack.pop();
    }


    private double factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }




    private boolean isNumeric(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }





    //=============================================================
    public void Change_Equation(){
        btn_euqation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_equation == false){
                    // Ẩn Layout 1 và hiện Layout 2
                    ll_number.setVisibility(View.GONE);
//                    ll_convert.setVisibility(View.GONE);
                    ll_euation.setVisibility(View.VISIBLE);
                    check_equation = true;
                }
                else{
                    // Ẩn Layout 2 và hiện Layout 1
                    ll_euation.setVisibility(View.GONE);
//                    ll_convert.setVisibility(View.GONE);
                    ll_number.setVisibility(View.VISIBLE);
                    check_equation = false;
                }

            }
        });

        ibtn_return_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_equation == false){
                    // Ẩn Layout 1 và hiện Layout 2
                    ll_number.setVisibility(View.GONE);
//                    ll_convert.setVisibility(View.GONE);
                    ll_euation.setVisibility(View.VISIBLE);
                    check_equation = true;
                }
                else{
                    // Ẩn Layout 2 và hiện Layout 1
                    ll_euation.setVisibility(View.GONE);
//                    ll_convert.setVisibility(View.GONE);
                    ll_number.setVisibility(View.VISIBLE);
                    check_equation = false;
                }
            }
        });



    }
//    public void Change_Convert(){
//        btn_convert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(check_convert == false){
//                    ll_convert.setVisibility(View.VISIBLE);
//                    ll_euation.setVisibility(View.GONE);
//                    ll_number.setVisibility(View.GONE);
//                    check_convert = true;
//                }
//                else {
//                    ll_convert.setVisibility(View.GONE);
//                    ll_euation.setVisibility(View.GONE);
//                    ll_number.setVisibility(View.VISIBLE);
//                    check_convert = false;
//                }
//            }
//        });
//    }

}