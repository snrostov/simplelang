## SimpleLang

Simple programming language for learning compiler theory.

### Model

All expressions is pure functions calls (result of function is defined only by arguments, no side effect).
Constant is function with 0 args.
Conditional expression and operators `+`, `-`, `*`, `/`, `%`, `and`, `or`, `not`, `>`, `=`, `<` are base functions.
All other expression is composition of base function calls.
Expressions may be extracted to user functions, which can be called himself recursive.
Loop is special case for recursive tail call of user function.

### Examples

See examples in [tests](https://github.com/snrostov/simplelang/tree/master/src/test/kotlin/org/srostov/simplelang/visitor/optimize).

### Table of contents
 
  - [x] [Model](src/main/kotlin/org/srostov/simplelang/Model.kt) - Point to start
  - [x] [Visitor]() - Main logic
      - [x] [Eval]() - Evaluate program
      - [x] [Print]() - Print program
      - [ ] [Optimize]() - Basic optimizations
          - [x] [Const]() - Constant fold and propogation:
              - `2 + 2` => `2`
              - `if (true) x else y` => `x`
              - `f(const, const)` => `eval f`        
          - [x] [Ops]() - Simple base operations optimization ( `a * 0 => 0`; `a * 1 => 1`; ... )
          - [ ] [CSE]() - Common subexpression elimination
          - [ ] [If]() - Eliminate known expression checks
          - [x] [Inline]() - Inline function for further processing ( `f(x, y) = x + x; f(2, 0)` => `2 + 0` => `2` )
          - [ ] [TailRecursion]() - Recursive tail call => loop
          - [ ] [Loop]() - Simple loop optimizations 
            - [ ] [LoopCollapse]()
            - [ ] [LoopFusion]()
            - [ ] [LoopHoisting]()
            - [ ] [LoopInvariant]()
            - [ ] [LoopUnroll]()
            - [ ] [LoopUnswitch]()
      - [ ] [Emit]() - Machine code generation
          - [ ] [RegAlloc]() - Register allocation
          - [ ] [x86_64]() - Transform operations to x86_64 instructions

